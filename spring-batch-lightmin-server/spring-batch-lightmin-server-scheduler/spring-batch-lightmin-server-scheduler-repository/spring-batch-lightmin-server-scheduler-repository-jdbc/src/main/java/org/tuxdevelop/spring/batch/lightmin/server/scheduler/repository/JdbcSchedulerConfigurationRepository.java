package org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobIncrementer;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.configuration.ServerSchedulerJdbcConfigurationProperties;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.SchedulerConfiguration;
import org.tuxdevelop.spring.batch.lightmin.util.DomainParameterParser;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JdbcSchedulerConfigurationRepository implements SchedulerConfigurationRepository {

    private final SchedulerConfigurationDAO schedulerConfigurationDAO;
    private final SchedulerConfigurationValueDAO schedulerConfigurationValueDAO;

    public JdbcSchedulerConfigurationRepository(final JdbcTemplate jdbcTemplate,
                                                final ServerSchedulerJdbcConfigurationProperties properties) {
        this.schedulerConfigurationValueDAO =
                new SchedulerConfigurationValueDAO(
                        jdbcTemplate,
                        properties.getConfigurationValueTable());
        this.schedulerConfigurationDAO =
                new SchedulerConfigurationDAO(
                        jdbcTemplate,
                        properties.getConfigurationTable(),
                        this.schedulerConfigurationValueDAO,
                        properties.getDatabaseSchema());


    }

    @Override
    public SchedulerConfiguration save(final SchedulerConfiguration schedulerConfiguration) {
        final SchedulerConfiguration result;

        if (schedulerConfiguration.getId() != null) {
            result = this.schedulerConfigurationDAO.update(schedulerConfiguration);
        } else {
            result = this.schedulerConfigurationDAO.save(schedulerConfiguration);
        }
        return result;
    }

    @Override
    public SchedulerConfiguration findById(final Long id) {
        return this.schedulerConfigurationDAO.findById(id);
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
    public List<SchedulerConfiguration> findByApplication(final String application) {
        return this.schedulerConfigurationDAO.findByApplication(application);
    }

    // ###########################
    // SchedulerConfigurationDAO
    // ###########################

    private static class SchedulerConfigurationDAO {

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

        private final JdbcTemplate jdbcTemplate;
        private final SimpleJdbcInsert simpleJdbcInsert;
        private final String tableName;
        private final SchedulerConfigurationValueDAO schedulerConfigurationValueDAO;
        private final SchedulerConfigurationRowMapper rowMapper;

        private SchedulerConfigurationDAO(final JdbcTemplate jdbcTemplate,
                                          final String tableName,
                                          final SchedulerConfigurationValueDAO schedulerConfigurationValueDAO,
                                          final String schema) {
            this.jdbcTemplate = jdbcTemplate;
            this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                    .withTableName(tableName)
                    .withSchemaName(schema)
                    .usingGeneratedKeyColumns(SchedulerConfigurationDomain.ID);
            this.tableName = tableName;
            this.schedulerConfigurationValueDAO = schedulerConfigurationValueDAO;
            this.rowMapper = new SchedulerConfigurationRowMapper();
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
            this.schedulerConfigurationValueDAO.attachValues(schedulerConfiguration);
            return schedulerConfiguration;
        }

        SchedulerConfiguration save(final SchedulerConfiguration schedulerConfiguration) {
            final Map<String, Object> keys = new HashMap<>();
            keys.put(SchedulerConfigurationDomain.APPLICATION_NAME, schedulerConfiguration.getApplication());
            keys.put(SchedulerConfigurationDomain.JOB_NAME, schedulerConfiguration.getJobName());
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

        // ########
        // Domain
        // ########

        private static final class SchedulerConfigurationDomain {

            private SchedulerConfigurationDomain() {
            }

            final static String ID = "id";
            final static String APPLICATION_NAME = "application_name";
            final static String JOB_NAME = "job_name";
        }

        private class SchedulerConfigurationRowMapper implements RowMapper<SchedulerConfiguration> {

            @Override
            public SchedulerConfiguration mapRow(final ResultSet resultSet, final int i) throws SQLException {
                final SchedulerConfiguration schedulerConfiguration = new SchedulerConfiguration();
                schedulerConfiguration.setId(resultSet.getLong(SchedulerConfigurationDomain.ID));
                schedulerConfiguration.setJobName(resultSet.getString(SchedulerConfigurationDomain.JOB_NAME));
                schedulerConfiguration.setApplication(resultSet.getString(SchedulerConfigurationDomain.APPLICATION_NAME));
                return schedulerConfiguration;
            }
        }


    }


    // ###############################
    // SchedulerConfigurationValueDAO
    // ###############################

    @Slf4j
    private static class SchedulerConfigurationValueDAO {


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

            final List<SchedulerConfigurationValue> values = mapSchedulerConfigurationValues(schedulerConfiguration);
            mapSchedulerConfigurationValues(schedulerConfiguration);

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

            //retriable
            final SchedulerConfigurationValue retriableValue = new SchedulerConfigurationValue();
            retriableValue.setType(TypeDomain.RETRIABLE);
            retriableValue.setSchedulerConfigurationid(schedulerConfiguration.getId());
            retriableValue.setValue(String.valueOf(schedulerConfiguration.getRetriable()));

            values.add(retriableValue);

            //max retries
            final SchedulerConfigurationValue maxRetriesValue = new SchedulerConfigurationValue();
            maxRetriesValue.setType(TypeDomain.MAX_RETRIES);
            maxRetriesValue.setSchedulerConfigurationid(schedulerConfiguration.getId());
            maxRetriesValue.setValue(String.valueOf(schedulerConfiguration.getMaxRetries()));

            values.add(maxRetriesValue);

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
                        attachParameter(schedulerConfiguration, value);
                        break;
                    }
                    case TypeDomain.CRON_EXPRESSION: {
                        schedulerConfiguration.setCronExpression(value.getValue());
                        break;
                    }
                    case TypeDomain.INSTANCE_EXECUTION_COUNT: {
                        schedulerConfiguration.setInstanceExecutionCount(Integer.parseInt(value.getValue()));
                        break;
                    }
                    case TypeDomain.RETRIABLE: {
                        schedulerConfiguration.setRetriable(Boolean.parseBoolean(value.getValue()));
                        break;
                    }
                    case TypeDomain.MAX_RETRIES: {
                        schedulerConfiguration.setMaxRetries(Integer.parseInt(value.getValue()));
                        break;
                    }
                    case TypeDomain.INCREMENTER: {
                        schedulerConfiguration.setJobIncrementer(JobIncrementer.valueOf(value.getValue()));
                        break;
                    }
                    default: {
                        //TODO: add specific Exception
                        throw new RuntimeException("");
                    }
                }
            }
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
            static final String RETRIABLE = "RETRIABLE";
            static final String MAX_RETRIES = "MAX_RETRIES";
            static final String INSTANCE_EXECUTION_COUNT = "INSTANCE_EXECUTION_COUNT";
            static final String INCREMENTER = "INCREMENTER";

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
