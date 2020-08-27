package org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobIncrementer;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.configuration.ServerSchedulerJdbcConfigurationProperties;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.SchedulerConfiguration;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.SchedulerValidationException;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.ServerSchedulerStatus;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.exception.SchedulerConfigurationNotFoundException;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.exception.SchedulerRepositoryException;
import org.tuxdevelop.spring.batch.lightmin.util.DomainParameterParser;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;

public class JdbcSchedulerConfigurationRepository implements SchedulerConfigurationRepository {

    private final SchedulerConfigurationDAO schedulerConfigurationDAO;
    private final SchedulerConfigurationValueDAO schedulerConfigurationValueDAO;

    public JdbcSchedulerConfigurationRepository(final JdbcTemplate jdbcTemplate,
                                                final ServerSchedulerJdbcConfigurationProperties properties) {
        this.schedulerConfigurationValueDAO =
                new SchedulerConfigurationValueDAO(
                        jdbcTemplate,
                        properties.getConfigurationValueTable());
        try {
            this.schedulerConfigurationDAO =
                    new SchedulerConfigurationDAO(
                            jdbcTemplate,
                            properties.getConfigurationTable(),
                            this.schedulerConfigurationValueDAO,
                            properties.getDatabaseSchema());
        } catch (final Exception e) {
            throw new SchedulerRepositoryException(e);
        }
    }

    @Override
    public SchedulerConfiguration save(final SchedulerConfiguration schedulerConfiguration) {
        final SchedulerConfiguration result;
        if (schedulerConfiguration != null) {
            if (schedulerConfiguration.getId() != null) {
                result = this.schedulerConfigurationDAO.update(schedulerConfiguration);
            } else {
                result = this.schedulerConfigurationDAO.save(schedulerConfiguration);
            }
            return result;
        } else {
            throw new SchedulerValidationException("schedulerConfiguration must not be null");
        }
    }

    @Override
    public SchedulerConfiguration findById(final Long id) throws SchedulerConfigurationNotFoundException {
        try {
            return this.schedulerConfigurationDAO.findById(id);
        } catch (final Exception e) {
            throw new SchedulerConfigurationNotFoundException("Could not find SchedulerConfiguration for id " + id, e);
        }
    }

    @Override
    public void delete(final Long id) {
        this.schedulerConfigurationDAO.deleteById(id);
    }

    @Override
    public List<SchedulerConfiguration> findAll() {
        return this.schedulerConfigurationDAO.findAll();
    }

    @Override
    public List<SchedulerConfiguration> findAll(final int startIndex, final int pageSize) {
        return this.schedulerConfigurationDAO.findAll(startIndex, pageSize);
    }

    @Override
    public List<SchedulerConfiguration> findByApplication(final String application) {
        return this.schedulerConfigurationDAO.findByApplication(application);
    }

    @Override
    public Integer getCount() {
        return this.schedulerConfigurationDAO.getCount();
    }

    // ###########################
    // SchedulerConfigurationDAO
    // ###########################

    private static class SchedulerConfigurationDAO {

        private static final String FIELDS = "S.id, S.application_name, S.job_name, S.configuration_status";

        private static final String GET_ALL =
                "SELECT * FROM %s";

        private static final String FIND_BY_APPLICATION =
                "SELECT * FROM %s WHERE "
                        + SchedulerConfigurationDomain.APPLICATION_NAME + " = ?";

        private static final String FIND_BY_ID =
                "SELECT * FROM %s WHERE "
                        + SchedulerConfigurationDomain.ID + " = ?";

        private static final String DELETE_BY_ID =
                "DELETE FROM %s WHERE "
                        + SchedulerConfigurationDomain.ID + " = ?";

        private static final String COUNT =
                "SELECT COUNT(1) FROM %s";

        private static final String UPDATE_STATUS =
                "UPDATE %s SET "
                        + SchedulerConfigurationDomain.CONFIGURATION_STATUS
                        + " = ? "
                        + " WHERE " + SchedulerConfigurationDomain.ID
                        + " = ? ";

        private final JdbcTemplate jdbcTemplate;
        private final SimpleJdbcInsert simpleJdbcInsert;
        private final String tableName;
        private final SchedulerConfigurationValueDAO schedulerConfigurationValueDAO;
        private final SchedulerConfigurationRowMapper rowMapper;
        private final PagingQueryProvider findAllPagingQueryProvider;

        private SchedulerConfigurationDAO(final JdbcTemplate jdbcTemplate,
                                          final String tableName,
                                          final SchedulerConfigurationValueDAO schedulerConfigurationValueDAO,
                                          final String schema) throws Exception {
            this.jdbcTemplate = jdbcTemplate;
            this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                    .withTableName(tableName)
                    .withSchemaName(schema)
                    .usingGeneratedKeyColumns(SchedulerConfigurationDomain.ID);
            this.tableName = tableName;
            this.schedulerConfigurationValueDAO = schedulerConfigurationValueDAO;
            this.rowMapper = new SchedulerConfigurationRowMapper();
            this.findAllPagingQueryProvider = this.getPagingQueryProvider(null);
        }

        SchedulerConfiguration findById(final Long id) {
            final String query = String.format(FIND_BY_ID, this.tableName);
            final SchedulerConfiguration schedulerConfiguration = this.jdbcTemplate.queryForObject(
                    query,
                    new Object[]{id},
                    new int[]{Types.NUMERIC},
                    this.rowMapper
            );
            this.schedulerConfigurationValueDAO.attachValues(schedulerConfiguration);
            return schedulerConfiguration;
        }

        List<SchedulerConfiguration> findAll() {
            final String query = String.format(GET_ALL, this.tableName);

            final List<SchedulerConfiguration> schedulerConfigurations = this.jdbcTemplate.query(
                    query,
                    this.rowMapper
            );
            for (final SchedulerConfiguration schedulerConfiguration : schedulerConfigurations) {
                this.schedulerConfigurationValueDAO.attachValues(schedulerConfiguration);
            }
            return schedulerConfigurations;
        }

        List<SchedulerConfiguration> findAll(final int startIndex, final int pageSize) {
            if (startIndex <= 0) {
                final List<SchedulerConfiguration> schedulerConfigurations =
                        this.jdbcTemplate.query(this.findAllPagingQueryProvider.generateFirstPageQuery(pageSize),
                                this.rowMapper);
                for (final SchedulerConfiguration schedulerConfiguration : schedulerConfigurations) {
                    this.schedulerConfigurationValueDAO.attachValues(schedulerConfiguration);
                }
                return schedulerConfigurations;
            }
            try {
                final Long startAfterValue = this.jdbcTemplate.queryForObject(
                        this.findAllPagingQueryProvider.generateJumpToItemQuery(startIndex, startIndex), Long.class);
                final List<SchedulerConfiguration> schedulerConfigurations =
                        this.jdbcTemplate.query(this.findAllPagingQueryProvider.generateRemainingPagesQuery(pageSize),
                                this.rowMapper, startAfterValue);
                for (final SchedulerConfiguration schedulerConfiguration : schedulerConfigurations) {
                    this.schedulerConfigurationValueDAO.attachValues(schedulerConfiguration);
                }
                return schedulerConfigurations;
            } catch (final IncorrectResultSizeDataAccessException e) {
                return Collections.emptyList();
            }
        }

        List<SchedulerConfiguration> findByApplication(final String applicationName) {
            final String query = String.format(FIND_BY_APPLICATION, this.tableName);

            final List<SchedulerConfiguration> schedulerConfigurations = this.jdbcTemplate.query(
                    query,
                    new Object[]{applicationName},
                    new int[]{Types.VARCHAR},
                    this.rowMapper
            );
            for (final SchedulerConfiguration schedulerConfiguration : schedulerConfigurations) {
                this.schedulerConfigurationValueDAO.attachValues(schedulerConfiguration);
            }
            return schedulerConfigurations;
        }

        SchedulerConfiguration update(final SchedulerConfiguration schedulerConfiguration) {
            this.schedulerConfigurationValueDAO.updateValues(schedulerConfiguration);
            this.updateStatus(schedulerConfiguration.getId(), schedulerConfiguration.getStatus().getValue());
            this.schedulerConfigurationValueDAO.attachValues(schedulerConfiguration);
            return schedulerConfiguration;
        }

        SchedulerConfiguration save(final SchedulerConfiguration schedulerConfiguration) {
            final Map<String, Object> keys = new HashMap<>();
            keys.put(SchedulerConfigurationDomain.APPLICATION_NAME, schedulerConfiguration.getApplication());
            keys.put(SchedulerConfigurationDomain.JOB_NAME, schedulerConfiguration.getJobName());
            keys.put(SchedulerConfigurationDomain.CONFIGURATION_STATUS, schedulerConfiguration.getStatus().getValue());
            final Number id = this.simpleJdbcInsert.executeAndReturnKey(keys);
            schedulerConfiguration.setId(id.longValue());
            this.schedulerConfigurationValueDAO.saveValue(schedulerConfiguration);
            return schedulerConfiguration;
        }

        void deleteById(final Long id) {
            this.schedulerConfigurationValueDAO.deleteValues(id);

            final String sql = String.format(DELETE_BY_ID, this.tableName);

            this.jdbcTemplate.update(
                    sql,
                    new Object[]{id},
                    new int[]{Types.NUMERIC}
            );
        }

        private void updateStatus(final Long id, final String status) {
            final String sql = String.format(UPDATE_STATUS, this.tableName);
            this.jdbcTemplate.update(
                    sql,
                    new Object[]{status, id},
                    new int[]{Types.VARCHAR, Types.NUMERIC}
            );
        }

        Integer getCount() {
            final String query = String.format(COUNT, this.tableName);
            return this.jdbcTemplate.queryForObject(query, Integer.class);
        }

        private PagingQueryProvider getPagingQueryProvider(final String whereClause) throws Exception {
            return this.getPagingQueryProvider(null, whereClause);
        }

        private PagingQueryProvider getPagingQueryProvider(String fromClause, String whereClause) throws Exception {
            final SqlPagingQueryProviderFactoryBean factory = new SqlPagingQueryProviderFactoryBean();
            factory.setDataSource(this.jdbcTemplate.getDataSource());
            fromClause = "%s S" + (fromClause == null ? "" : ", " + fromClause);
            factory.setFromClause(String.format(fromClause, this.tableName));
            factory.setSelectClause(FIELDS);
            final Map<String, Order> sortKeys = new HashMap<>();
            sortKeys.put("id", Order.ASCENDING);
            factory.setSortKeys(sortKeys);
            whereClause = whereClause == null ? "" : whereClause;
            factory.setWhereClause(whereClause);
            return factory.getObject();
        }

        // ########
        // Domain
        // ########

        private static final class SchedulerConfigurationDomain {

            private SchedulerConfigurationDomain() {
            }

            final static String ID = "id";
            final static String APPLICATION_NAME = "application_name";
            final static String JOB_NAME = "job_name";
            final static String CONFIGURATION_STATUS = "configuration_status";
        }

        private class SchedulerConfigurationRowMapper implements RowMapper<SchedulerConfiguration> {

            @Override
            public SchedulerConfiguration mapRow(final ResultSet resultSet, final int i) throws SQLException {
                final SchedulerConfiguration schedulerConfiguration = new SchedulerConfiguration();
                schedulerConfiguration.setId(resultSet.getLong(SchedulerConfigurationDomain.ID));
                schedulerConfiguration.setJobName(resultSet.getString(SchedulerConfigurationDomain.JOB_NAME));
                schedulerConfiguration.setApplication(resultSet.getString(SchedulerConfigurationDomain.APPLICATION_NAME));
                schedulerConfiguration.setStatus(ServerSchedulerStatus.getByValue(resultSet.getString(SchedulerConfigurationDomain.CONFIGURATION_STATUS)));
                return schedulerConfiguration;
            }
        }


    }


    // ###############################
    // SchedulerConfigurationValueDAO
    // ###############################

    @Slf4j
    private static class SchedulerConfigurationValueDAO {

        private static final String NULL_VALUE = "null";

        private static final String FIND_BY_JSC_ID =
                "SELECT * FROM %s WHERE "
                        + SchedulerConfigurationValueDomain.SCHEDULER_CONFIGURATION_ID + " = ?";

        private static final String INSERT_VALUES =
                "INSERT INTO %s ("
                        + SchedulerConfigurationValueDomain.SCHEDULER_CONFIGURATION_ID + ","
                        + SchedulerConfigurationValueDomain.TYPE + ", "
                        + SchedulerConfigurationValueDomain.VALUE + ")"
                        + " VALUES (?,?,?)";

        private static final String DELETE_VALUES_FOR_JSC_ID =
                "DELETE FROM %s WHERE "
                        + SchedulerConfigurationValueDomain.SCHEDULER_CONFIGURATION_ID + " = ?";

        private final JdbcTemplate jdbcTemplate;
        private final String tableName;
        private final SchedulderConfigurationValueRowMapper rowMapper;

        public SchedulerConfigurationValueDAO(final JdbcTemplate jdbcTemplate, final String tableName) {
            this.jdbcTemplate = jdbcTemplate;
            this.tableName = tableName;
            this.rowMapper = new SchedulderConfigurationValueRowMapper();
        }

        void saveValue(final SchedulerConfiguration schedulerConfiguration) {

            final List<SchedulerConfigurationValue> values = this.mapSchedulerConfigurationValues(schedulerConfiguration);
            this.mapSchedulerConfigurationValues(schedulerConfiguration);

            final String sql = String.format(INSERT_VALUES, this.tableName);
            this.jdbcTemplate.batchUpdate(
                    sql,
                    new BatchPreparedStatementSetter() {
                        @Override
                        public void setValues(final PreparedStatement preparedStatement, final int i) throws SQLException {
                            final SchedulerConfigurationValue value = values.get(i);
                            preparedStatement.setLong(1, value.getSchedulerConfigurationid());
                            preparedStatement.setString(2, value.getType());
                            preparedStatement.setString(3, value.getValue());
                        }

                        @Override
                        public int getBatchSize() {
                            return values.size();
                        }
                    }
            );
        }

        void updateValues(final SchedulerConfiguration schedulerConfiguration) {
            this.deleteValues(schedulerConfiguration.getId());
            this.saveValue(schedulerConfiguration);
        }

        void deleteValues(final Long schedulerConfigurationId) {
            final String sql = String.format(DELETE_VALUES_FOR_JSC_ID, this.tableName);
            this.jdbcTemplate.update(
                    sql,
                    new Object[]{schedulerConfigurationId},
                    new int[]{Types.NUMERIC});
        }

        private List<SchedulerConfigurationValue> mapSchedulerConfigurationValues(
                final SchedulerConfiguration schedulerConfiguration) {

            final List<SchedulerConfigurationValue> values = new ArrayList<>();
            //cron
            final SchedulerConfigurationValue cronValue = new SchedulerConfigurationValue();
            cronValue.setType(TypeDomain.CRON_EXPRESSION);
            cronValue.setSchedulerConfigurationid(schedulerConfiguration.getId());
            cronValue.setValue(schedulerConfiguration.getCronExpression());

            values.add(cronValue);

            //instance count
            final SchedulerConfigurationValue instanceCountValue = new SchedulerConfigurationValue();
            instanceCountValue.setType(TypeDomain.INSTANCE_EXECUTION_COUNT);
            instanceCountValue.setSchedulerConfigurationid(schedulerConfiguration.getId());
            instanceCountValue.setValue(String.valueOf(schedulerConfiguration.getInstanceExecutionCount()));

            values.add(instanceCountValue);

            //retryable
            final SchedulerConfigurationValue retryableValue = new SchedulerConfigurationValue();
            retryableValue.setType(TypeDomain.RETRYABLE);
            retryableValue.setSchedulerConfigurationid(schedulerConfiguration.getId());
            retryableValue.setValue(String.valueOf(schedulerConfiguration.getRetryable()));

            values.add(retryableValue);

            //max retries
            final SchedulerConfigurationValue maxRetriesValue = new SchedulerConfigurationValue();
            maxRetriesValue.setType(TypeDomain.MAX_RETRIES);
            maxRetriesValue.setSchedulerConfigurationid(schedulerConfiguration.getId());
            maxRetriesValue.setValue(String.valueOf(schedulerConfiguration.getMaxRetries()));

            values.add(maxRetriesValue);

            //retry interval
            final SchedulerConfigurationValue retryIntervalValue = new SchedulerConfigurationValue();
            retryIntervalValue.setType(TypeDomain.RETRY_INTERVAL);
            retryIntervalValue.setSchedulerConfigurationid(schedulerConfiguration.getId());
            retryIntervalValue.setValue(String.valueOf(schedulerConfiguration.getRetryInterval()));

            values.add(retryIntervalValue);

            //incrementer
            final SchedulerConfigurationValue incrementerValue = new SchedulerConfigurationValue();
            incrementerValue.setType(TypeDomain.INCREMENTER);
            incrementerValue.setSchedulerConfigurationid(schedulerConfiguration.getId());
            incrementerValue.setValue(schedulerConfiguration.getJobIncrementer().name());

            values.add(incrementerValue);

            //jobParameters
            final Map<String, Object> jobParameters = schedulerConfiguration.getJobParameters();

            if (jobParameters != null && !jobParameters.isEmpty()) {

                for (final Map.Entry<String, Object> entry : jobParameters.entrySet()) {
                    final StringBuilder stringBuilder = new StringBuilder();
                    DomainParameterParser.parseParameterEntryToString(stringBuilder, entry);
                    final String value = stringBuilder.toString();
                    final SchedulerConfigurationValue jobParameterValue = new SchedulerConfigurationValue();
                    jobParameterValue.setType(TypeDomain.JOB_PARAMETER);
                    jobParameterValue.setSchedulerConfigurationid(schedulerConfiguration.getId());
                    jobParameterValue.setValue(value);
                    values.add(jobParameterValue);
                }
            } else {
                log.debug("No job parameters available to map");
            }
            return values;
        }

        void attachValues(final SchedulerConfiguration schedulerConfiguration) {
            final List<SchedulerConfigurationValue> values =
                    this.getSchedulerConfigurationValues(schedulerConfiguration.getId());

            for (final SchedulerConfigurationValue value : values) {

                switch (value.getType()) {
                    case TypeDomain.JOB_PARAMETER: {
                        this.attachParameter(schedulerConfiguration, value);
                        break;
                    }
                    case TypeDomain.CRON_EXPRESSION: {
                        schedulerConfiguration.setCronExpression(value.getValue());
                        break;
                    }
                    case TypeDomain.INSTANCE_EXECUTION_COUNT: {
                        schedulerConfiguration.setInstanceExecutionCount(this.mapInputToInteger(value.getValue()));
                        break;
                    }
                    case TypeDomain.RETRYABLE: {
                        schedulerConfiguration.setRetryable(Boolean.parseBoolean(value.getValue()));
                        break;
                    }
                    case TypeDomain.MAX_RETRIES: {
                        schedulerConfiguration.setMaxRetries(this.mapInputToInteger(value.getValue()));
                        break;
                    }
                    case TypeDomain.INCREMENTER: {
                        schedulerConfiguration.setJobIncrementer(JobIncrementer.valueOf(value.getValue()));
                        break;
                    }
                    case TypeDomain.RETRY_INTERVAL: {
                        schedulerConfiguration.setRetryInterval(this.mapInputToLong(value.getValue()));
                        break;
                    }
                    default: {
                        throw new SchedulerRepositoryException("could not map data base domain type: " + value.getType());
                    }
                }
            }
        }

        private Long mapInputToLong(final String input) {
            final Long result;
            if (input == null) {
                result = null;
            } else {
                if (NULL_VALUE.equals(input)) {
                    result = null;
                } else {
                    result = Long.parseLong(input);
                }
            }
            return result;
        }

        private Integer mapInputToInteger(final String input) {
            final Integer result;
            if (input == null) {
                result = null;
            } else {
                if (NULL_VALUE.equals(input)) {
                    result = null;
                } else {
                    result = Integer.parseInt(input);
                }
            }
            return result;
        }

        private void attachParameter(final SchedulerConfiguration schedulerConfiguration,
                                     final SchedulerConfigurationValue value) {
            final Map.Entry<String, Object> parameter = DomainParameterParser.generateParameterEntry(value.getValue());
            final Map<String, Object> parameters;
            if (schedulerConfiguration.getJobParameters() == null) {
                parameters = new HashMap<>();
            } else {
                parameters = schedulerConfiguration.getJobParameters();
            }
            parameters.put(parameter.getKey(), parameter.getValue());
            schedulerConfiguration.setJobParameters(parameters);
        }

        private List<SchedulerConfigurationValue> getSchedulerConfigurationValues(
                final Long schedulerConfigurationId) {
            final String query = String.format(FIND_BY_JSC_ID, this.tableName);

            return this.jdbcTemplate.query(
                    query,
                    new Object[]{schedulerConfigurationId},
                    new int[]{Types.NUMERIC},
                    this.rowMapper
            );
        }

        // ########
        // Domain
        // ########

        @Data
        private class SchedulerConfigurationValue {
            private Long id;
            private Long schedulerConfigurationid;
            private String type;
            private String value;
        }

        private static final class TypeDomain {

            static final String JOB_PARAMETER = "JOB_PARAMETER";
            static final String CRON_EXPRESSION = "CRON_EXPRESSION";
            static final String RETRYABLE = "RETRYABLE";
            static final String MAX_RETRIES = "MAX_RETRIES";
            static final String INSTANCE_EXECUTION_COUNT = "INSTANCE_EXECUTION_COUNT";
            static final String INCREMENTER = "INCREMENTER";
            static final String RETRY_INTERVAL = "RETRY_INTERVAL";

        }

        private static final class SchedulerConfigurationValueDomain {

            private SchedulerConfigurationValueDomain() {
            }

            final static String ID = "id";
            final static String SCHEDULER_CONFIGURATION_ID = "scheduler_configuration_id";
            final static String TYPE = "type";
            final static String VALUE = "value";
        }

        private class SchedulderConfigurationValueRowMapper implements RowMapper<SchedulerConfigurationValue> {

            @Override
            public SchedulerConfigurationValue mapRow(final ResultSet resultSet, final int i) throws SQLException {
                final SchedulerConfigurationValue value = new SchedulerConfigurationValue();
                value.setId(resultSet.getLong(SchedulerConfigurationValueDomain.ID));
                value.setSchedulerConfigurationid(resultSet.getLong(SchedulerConfigurationValueDomain.SCHEDULER_CONFIGURATION_ID));
                value.setType(resultSet.getString(SchedulerConfigurationValueDomain.TYPE));
                value.setValue(resultSet.getString(SchedulerConfigurationValueDomain.VALUE));
                return value;
            }
        }

    }
}
