package org.tuxdevelop.spring.batch.lightmin.repository;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.tuxdevelop.spring.batch.lightmin.domain.*;
import org.tuxdevelop.spring.batch.lightmin.exception.NoSuchJobConfigurationException;
import org.tuxdevelop.spring.batch.lightmin.exception.NoSuchJobException;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminApplicationException;
import org.tuxdevelop.spring.batch.lightmin.repository.configuration.JdbcJobConfigurationRepositoryConfigurationProperties;
import org.tuxdevelop.spring.batch.lightmin.util.DomainParameterParser;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Marcel Becker
 * @since 0.1
 */
@Slf4j
public class JdbcJobConfigurationRepository implements JobConfigurationRepository, InitializingBean {

    private final JdbcTemplate jdbcTemplate;
    private final JobConfigurationDAO jobConfigurationDAO;
    private final JobConfigurationParameterDAO jobConfigurationParameterDAO;
    private final JobConfigurationValueDAO jobConfigurationValueDAO;

    public JdbcJobConfigurationRepository(
            final JdbcTemplate jdbcTemplate,
            final JdbcJobConfigurationRepositoryConfigurationProperties springBatchLightminConfigurationProperties) {

        this(jdbcTemplate,
                springBatchLightminConfigurationProperties.getJobConfigurationTableName(),
                springBatchLightminConfigurationProperties.getJobConfigurationValueTableName(),
                springBatchLightminConfigurationProperties.getJobConfigurationParameterTableName(),
                springBatchLightminConfigurationProperties.getConfigurationDatabaseSchema());

    }

    public JdbcJobConfigurationRepository(final JdbcTemplate jdbcTemplate,
                                          final String jobConfigurationTableName,
                                          final String jobConfigurationValueTableName,
                                          final String jobConfigurationParameterTableName,
                                          final String schema) {
        log.debug("Initializing JdbcJobConfigurationRepository with tables names: {} , {} , {}", jobConfigurationTableName, jobConfigurationValueTableName, jobConfigurationParameterTableName);
        this.jdbcTemplate = jdbcTemplate;
        this.jobConfigurationDAO = new JobConfigurationDAO(jdbcTemplate, jobConfigurationTableName, schema);
        this.jobConfigurationParameterDAO = new JobConfigurationParameterDAO(jdbcTemplate, jobConfigurationParameterTableName, schema);
        this.jobConfigurationValueDAO = new JobConfigurationValueDAO(jdbcTemplate, jobConfigurationValueTableName);
    }

    @Override
    public JobConfiguration getJobConfiguration(final Long jobConfigurationId, final String applicationName) throws
            NoSuchJobConfigurationException {
        if (this.checkJobConfigurationExists(jobConfigurationId, applicationName)) {
            final JobConfigurationDAO.JobConfigurationJdbcWrapper jobConfigurationJdbcWrapper = this.jobConfigurationDAO.getById(jobConfigurationId, applicationName);
            final JobConfiguration jobConfiguration = jobConfigurationJdbcWrapper.getJobConfiguration();
            this.jobConfigurationValueDAO.attachConfigurationValues(jobConfiguration, jobConfigurationJdbcWrapper.jobConfigurationType);
            this.jobConfigurationParameterDAO.attachParameters(jobConfiguration);
            return jobConfiguration;
        } else {
            final String message = "No jobConfiguration could be found for id:" + jobConfigurationId;
            log.error(message);
            throw new NoSuchJobConfigurationException(message);
        }
    }

    @Override
    public Collection<JobConfiguration> getJobConfigurations(final String jobName, final String applicationName) throws NoSuchJobException {
        if (this.checkJobConfigurationExists(jobName, applicationName)) {
            final List<JobConfigurationDAO.JobConfigurationJdbcWrapper> jobConfigurationJdbcWrappers = this.jobConfigurationDAO.getByJobName(jobName, applicationName);
            final List<JobConfiguration> jobConfigurations = new ArrayList<>();
            this.mapJobConfigurations(jobConfigurationJdbcWrappers, jobConfigurations);
            return jobConfigurations;
        } else {
            final String message = "No jobConfiguration could be found for jobName:" + jobName;
            log.error(message);
            throw new NoSuchJobException(message);
        }
    }

    @Override
    public JobConfiguration add(final JobConfiguration jobConfiguration, final String applicationName) {
        final Long jobConfigurationId = this.jobConfigurationDAO.add(jobConfiguration, applicationName);
        jobConfiguration.setJobConfigurationId(jobConfigurationId);
        this.jobConfigurationValueDAO.addConfigurationValues(jobConfiguration);
        this.jobConfigurationParameterDAO.add(jobConfiguration);
        return jobConfiguration;
    }

    @Override
    public JobConfiguration update(final JobConfiguration jobConfiguration, final String applicationName) throws NoSuchJobConfigurationException {
        final Long jobConfigurationId = jobConfiguration.getJobConfigurationId();
        if (this.checkJobConfigurationExists(jobConfigurationId, applicationName)) {
            this.jobConfigurationDAO.update(jobConfiguration, applicationName);
            this.jobConfigurationValueDAO.update(jobConfiguration);
            this.jobConfigurationParameterDAO.delete(jobConfigurationId);
            this.jobConfigurationParameterDAO.add(jobConfiguration);
            return jobConfiguration;
        } else {
            final String message = "No jobConfiguration could be found for id:" + jobConfiguration;
            log.error(message);
            throw new NoSuchJobConfigurationException(message);
        }
    }

    @Override
    public void delete(final JobConfiguration jobConfiguration, final String applicationName) throws NoSuchJobConfigurationException {
        final Long jobConfigurationId = jobConfiguration.getJobConfigurationId();
        if (this.checkJobConfigurationExists(jobConfigurationId, applicationName)) {
            this.jobConfigurationParameterDAO.delete(jobConfigurationId);
            this.jobConfigurationValueDAO.delete(jobConfigurationId);
            this.jobConfigurationDAO.delete(jobConfigurationId, applicationName);
        } else {
            final String message = "No jobConfiguration could be found for id:" + jobConfiguration;
            log.error(message);
            throw new NoSuchJobConfigurationException(message);
        }
    }

    @Override
    public Collection<JobConfiguration> getAllJobConfigurations(final String applicationName) {
        final List<JobConfigurationDAO.JobConfigurationJdbcWrapper> jobConfigurationJdbcWrappers = this.jobConfigurationDAO.getAll(applicationName);
        final List<JobConfiguration> jobConfigurations = new ArrayList<>();
        this.mapJobConfigurations(jobConfigurationJdbcWrappers, jobConfigurations);
        return jobConfigurations;
    }

    @Override
    public Collection<JobConfiguration> getAllJobConfigurationsByJobNames(final Collection<String> jobNames, final String applicationName) {
        final List<JobConfigurationDAO.JobConfigurationJdbcWrapper> jobConfigurationJdbcWrappers = this.jobConfigurationDAO.getAllByJobNames(jobNames, applicationName);
        final List<JobConfiguration> jobConfigurations = new ArrayList<>();
        this.mapJobConfigurations(jobConfigurationJdbcWrappers, jobConfigurations);
        return jobConfigurations;
    }

    @Override
    public void afterPropertiesSet() {
        assert this.jdbcTemplate != null;
    }

    /*
     * -------------------------- HELPER CLASSES AND METHODS -------------------
     */

    private Boolean checkJobConfigurationExists(final Long jobConfigurationId, final String applicationName) {
        return this.jobConfigurationDAO.getJobConfigurationIdCount(jobConfigurationId, applicationName) > 0;
    }

    private Boolean checkJobConfigurationExists(final String jobName, final String applicationName) {
        return this.jobConfigurationDAO.getJobNameCount(jobName, applicationName) > 0;
    }

    private void mapJobConfigurations(final List<JobConfigurationDAO.JobConfigurationJdbcWrapper> jobConfigurationJdbcWrappers, final List<JobConfiguration> jobConfigurations) {
        for (final JobConfigurationDAO.JobConfigurationJdbcWrapper jobConfigurationJdbcWrapper : jobConfigurationJdbcWrappers) {
            final JobConfiguration jobConfiguration = jobConfigurationJdbcWrapper.getJobConfiguration();
            this.jobConfigurationValueDAO.attachConfigurationValues(jobConfiguration, jobConfigurationJdbcWrapper.jobConfigurationType);
            this.jobConfigurationParameterDAO.attachParameters(jobConfiguration);
            jobConfigurations.add(jobConfiguration);
        }
    }

    private static final class JobConfigurationType {
        private JobConfigurationType() {
        }

        static final Integer SCHEDULER = 1;
        static final Integer LISTENER = 2;

        static Integer determineJobConfigurationType(final JobConfiguration jobConfiguration) {
            final Integer jobConfigurationType;
            if (jobConfiguration.getJobSchedulerConfiguration() != null) {
                jobConfigurationType = JobConfigurationType.SCHEDULER;
            } else if (jobConfiguration.getJobListenerConfiguration() != null) {
                jobConfigurationType = JobConfigurationType.LISTENER;
            } else {
                throw new SpringBatchLightminApplicationException("Could not determine JobConfigurationType");
            }
            return jobConfigurationType;
        }
    }


    /*
     * ------------------------- DAOs ---------------------
     */


    /**
     *
     */
    private static class JobConfigurationDAO {

        private static final String TABLE_NAME = "%s";

        private static final String GET_JOB_CONFIGURATION_QUERY = "SELECT * FROM " + TABLE_NAME + " WHERE "
                + JobConfigurationDomain.JOB_CONFIGURATION_ID + " = ? AND " + JobConfigurationDomain.APLLICATION_NAME + " = ?";

        private static final String GET_JOB_CONFIGURATIONS_BY_JOB_NAME_QUERY = "SELECT * FROM " + TABLE_NAME
                + " WHERE " + JobConfigurationDomain.JOB_NAME + " = ? AND " + JobConfigurationDomain.APLLICATION_NAME + " = ?";

        private static final String UPDATE_STATEMENT = "UPDATE " + TABLE_NAME + " SET "
                + JobConfigurationDomain.JOB_NAME + " = ? , " + JobConfigurationDomain.JOB_INCREMENTER
                + " = ? WHERE " + JobConfigurationDomain.JOB_CONFIGURATION_ID + " = ? AND " + JobConfigurationDomain.APLLICATION_NAME + " = ?";

        private static final String DELETE_STATEMENT = "DELETE FROM " + TABLE_NAME + " WHERE "
                + JobConfigurationDomain.JOB_CONFIGURATION_ID + " = ? AND " + JobConfigurationDomain.APLLICATION_NAME + " = ?";

        private static final String GET_JOB_CONFIGURATION_ID_COUNT_STATEMENT = "SELECT COUNT(1) FROM " + TABLE_NAME
                + " WHERE" + " " + JobConfigurationDomain.JOB_CONFIGURATION_ID + " = ? AND " + JobConfigurationDomain.APLLICATION_NAME + " = ?";

        private static final String GET_JOB_NAME_COUNT_STATEMENT = "SELECT COUNT(1) FROM " + TABLE_NAME + " WHERE"
                + " " + JobConfigurationDomain.JOB_NAME + " = ? AND " + JobConfigurationDomain.APLLICATION_NAME + " = ?";

        private static final String GET_ALL_JOB_CONFIGURATION_QUERY = "SELECT * FROM " + TABLE_NAME + " WHERE " +
                JobConfigurationDomain.APLLICATION_NAME + " = ?";

        private static final String GET_ALL_JOB_CONFIGURATION_BY_JOB_NAMES_QUERY = "SELECT * FROM " + TABLE_NAME +
                " WHERE " + JobConfigurationDomain.APLLICATION_NAME + " = ? AND " + JobConfigurationDomain.JOB_NAME + " IN (%s)";

        private final JdbcTemplate jdbcTemplate;
        private final SimpleJdbcInsert simpleJdbcInsert;
        private final String tableName;
        private final JobConfigurationRowMapper jobConfigurationRowMapper;
        private final JobConfigurationJdbcWrapperRowMapper jobConfigurationJdbcWrapperRowMapper;

        JobConfigurationDAO(final JdbcTemplate jdbcTemplate, final String tableName, final String schema) {
            this.jdbcTemplate = jdbcTemplate;
            this.tableName = tableName;
            this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                    .withSchemaName(schema)
                    .withTableName(String.format(TABLE_NAME, tableName)).usingGeneratedKeyColumns(
                            JobConfigurationDomain.JOB_CONFIGURATION_ID);
            this.jobConfigurationRowMapper = new JobConfigurationRowMapper();
            this.jobConfigurationJdbcWrapperRowMapper = new JobConfigurationJdbcWrapperRowMapper(this.jobConfigurationRowMapper);
        }

        public Long add(final JobConfiguration jobConfiguration, final String applicationName) {
            final Map<String, ?> keyValues = this.map(jobConfiguration, applicationName);
            final Number key = this.simpleJdbcInsert.executeAndReturnKey(keyValues);
            return key.longValue();
        }

        JobConfigurationJdbcWrapper getById(final Long jobConfigurationId, final String applicationName) {
            final String sql = String.format(GET_JOB_CONFIGURATION_QUERY, this.tableName);
            return this.jdbcTemplate.queryForObject(sql, this.jobConfigurationJdbcWrapperRowMapper, jobConfigurationId, applicationName);
        }

        List<JobConfigurationJdbcWrapper> getByJobName(final String jobName, final String applicationName) {
            final String sql = String.format(GET_JOB_CONFIGURATIONS_BY_JOB_NAME_QUERY, this.tableName);
            return this.jdbcTemplate.query(sql, this.jobConfigurationJdbcWrapperRowMapper, jobName, applicationName);
        }

        public void update(final JobConfiguration jobConfiguration, final String applicationName) {
            final String sql = String.format(UPDATE_STATEMENT, this.tableName);
            this.jdbcTemplate.update(
                    sql,
                    new Object[]{jobConfiguration.getJobName(),
                            jobConfiguration.getJobIncrementer().getIncrementerIdentifier(),
                            jobConfiguration.getJobConfigurationId(), applicationName}, new int[]{Types.VARCHAR, Types.VARCHAR,
                            Types.NUMERIC, Types.VARCHAR});
        }

        public void delete(final Long jobConfigurationId, final String applicationName) {
            final String sql = String.format(DELETE_STATEMENT, this.tableName);
            this.jdbcTemplate.update(sql, new Object[]{jobConfigurationId, applicationName}, new int[]{Types.NUMERIC, Types.VARCHAR});
        }

        Long getJobConfigurationIdCount(final Long jobConfiguration, final String applicationName) {
            final String sql = String.format(GET_JOB_CONFIGURATION_ID_COUNT_STATEMENT, this.tableName);
            return this.jdbcTemplate.queryForObject(sql, new Object[]{jobConfiguration, applicationName}, new int[]{Types.NUMERIC, Types.VARCHAR}, Long.class);
        }

        Long getJobNameCount(final String jobName, final String applicationName) {
            final String sql = String.format(GET_JOB_NAME_COUNT_STATEMENT, this.tableName);
            return this.jdbcTemplate.queryForObject(sql, new Object[]{jobName, applicationName}, new int[]{Types.VARCHAR, Types.VARCHAR}, Long.class);
        }

        List<JobConfigurationJdbcWrapper> getAll(final String applicationName) {
            final String sql = String.format(GET_ALL_JOB_CONFIGURATION_QUERY, this.tableName);
            return this.jdbcTemplate.query(sql, this.jobConfigurationJdbcWrapperRowMapper, applicationName);
        }

        List<JobConfigurationJdbcWrapper> getAllByJobNames(final Collection<String> jobNames, final String applicationName) {
            final String inParameters = this.parseInCollection(jobNames);
            final String sql = String.format(GET_ALL_JOB_CONFIGURATION_BY_JOB_NAMES_QUERY, this.tableName, inParameters);
            final Object[] parameters = new Object[jobNames.size() + 1];
            parameters[0] = applicationName;
            final Object[] jobNamesArray = jobNames.toArray();
            System.arraycopy(jobNamesArray, 0, parameters, 1, jobNamesArray.length);
            return this.jdbcTemplate
                    .query(sql, this.jobConfigurationJdbcWrapperRowMapper, parameters);
        }

        private Map<String, Object> map(final JobConfiguration jobConfiguration, final String applicationName) {
            final Map<String, Object> keyValues = new HashMap<>();
            keyValues.put(JobConfigurationDomain.JOB_NAME, jobConfiguration.getJobName());
            keyValues.put(JobConfigurationDomain.JOB_INCREMENTER, jobConfiguration.getJobIncrementer()
                    .getIncrementerIdentifier());
            keyValues.put(JobConfigurationDomain.APLLICATION_NAME, applicationName);
            if (jobConfiguration.getJobConfigurationId() != null) {
                keyValues.put(JobConfigurationDomain.JOB_CONFIGURATION_ID, jobConfiguration.getJobConfigurationId());
            }
            final Integer jobConfigurationType = JobConfigurationType.determineJobConfigurationType(jobConfiguration);
            keyValues.put(JobConfigurationDomain.JOB_CONFIGURATION_TYPE, jobConfigurationType);
            return keyValues;
        }

        private String parseInCollection(final Collection<String> inParameters) {
            final StringBuilder stringBuilder = new StringBuilder();
            final Iterator<String> iterator = inParameters.iterator();
            while (iterator.hasNext()) {
                stringBuilder.append("?");
                iterator.next();
                if (iterator.hasNext()) {
                    stringBuilder.append(",");
                }
            }
            return stringBuilder.toString();
        }

        private class JobConfigurationJdbcWrapperRowMapper implements RowMapper<JobConfigurationJdbcWrapper> {

            private final JobConfigurationRowMapper jobConfigurationRowMapper;

            private JobConfigurationJdbcWrapperRowMapper(final JobConfigurationRowMapper jobConfigurationRowMapper) {
                this.jobConfigurationRowMapper = jobConfigurationRowMapper;
            }

            @Override
            public JobConfigurationJdbcWrapper mapRow(final ResultSet resultSet, final int i) throws SQLException {
                final JobConfiguration jobConfiguration = this.jobConfigurationRowMapper.mapRow(resultSet, i);
                final Integer jobConfigurationType = resultSet.getInt(JobConfigurationDomain.JOB_CONFIGURATION_TYPE);
                return new JobConfigurationJdbcWrapper(jobConfiguration, jobConfigurationType);
            }

        }

        private static class JobConfigurationRowMapper implements RowMapper<JobConfiguration> {

            @Override
            public JobConfiguration mapRow(final ResultSet resultSet, final int rowNum) throws SQLException {
                final JobConfiguration jobConfiguration = new JobConfiguration();
                jobConfiguration.setJobConfigurationId(resultSet.getLong(JobConfigurationDomain.JOB_CONFIGURATION_ID));
                jobConfiguration.setJobName(resultSet.getString(JobConfigurationDomain.JOB_NAME));
                final JobIncrementer jobIncrementer = JobIncrementer.getByIdentifier(resultSet
                        .getString(JobConfigurationDomain.JOB_INCREMENTER));
                jobConfiguration.setJobIncrementer(jobIncrementer);
                return jobConfiguration;
            }
        }

        private static final class JobConfigurationDomain {

            private JobConfigurationDomain() {
            }

            static final String JOB_CONFIGURATION_ID = "job_configuration_id";
            static final String APLLICATION_NAME = "application_name";
            static final String JOB_NAME = "job_name";
            static final String JOB_INCREMENTER = "job_incrementer";
            static final String JOB_CONFIGURATION_TYPE = "job_configuration_type";

        }


        @Data
        class JobConfigurationJdbcWrapper {
            private final JobConfiguration jobConfiguration;
            private final Integer jobConfigurationType;
        }
    }

    /**
     *
     */
    private static class JobConfigurationValueDAO {

        private static final String SELECT_VALUES_BY_JOB_CONFIGURATION_ID
                = "SELECT * FROM %s "
                + "WHERE " + ValueRecordDomain.JOB_CONFIGURATION_ID + " = ?";

        private static final String INSERT_VALUES
                = "INSERT INTO %s (" + ValueRecordDomain.JOB_CONFIGURATION_ID + "," + ValueRecordDomain.KEY + ", " + ValueRecordDomain.VALUE + ")"
                + " VALUES (?,?,?)";

        private static final String UPDATE_VALUES
                = "UPDATE %s SET "
                + ValueRecordDomain.VALUE + " = ? "
                + "WHERE " + ValueRecordDomain.JOB_CONFIGURATION_ID + " = ? "
                + " AND " + ValueRecordDomain.KEY + " = ?";

        private static final String DELETE_VALUES
                = "DELETE FROM %s "
                + "WHERE " + ValueRecordDomain.JOB_CONFIGURATION_ID + " = ?";

        private final JdbcTemplate jdbcTemplate;
        private final String tableName;
        private final ValueRecordRowMapper valueRecordRowMapper;

        JobConfigurationValueDAO(final JdbcTemplate jdbcTemplate, final String tableName) {
            this.jdbcTemplate = jdbcTemplate;
            this.tableName = tableName;
            this.valueRecordRowMapper = new ValueRecordRowMapper();
        }

        void attachConfigurationValues(final JobConfiguration jobConfiguration, final Integer configurationType) {
            final String sql = String.format(SELECT_VALUES_BY_JOB_CONFIGURATION_ID, this.tableName);
            final List<ValueRecord> valueRecords = this.jdbcTemplate.query(
                    sql,
                    new Object[]{jobConfiguration.getJobConfigurationId()},
                    new int[]{Types.NUMERIC},
                    this.valueRecordRowMapper);
            this.attachByConfigurationType(jobConfiguration, configurationType, valueRecords);
        }

        void addConfigurationValues(final JobConfiguration jobConfiguration) {
            final List<ValueRecord> valueRecords = this.getValueRecords(jobConfiguration);
            final String sql = String.format(INSERT_VALUES, this.tableName);
            this.jdbcTemplate.batchUpdate(
                    sql,
                    new BatchPreparedStatementSetter() {
                        @Override
                        public void setValues(final PreparedStatement preparedStatement, final int i) throws SQLException {
                            final ValueRecord valueRecord = valueRecords.get(i);
                            final String value = valueRecord.getValue() != null ? valueRecord.getValue().toString() : null;
                            preparedStatement.setLong(1, valueRecord.getJobConfigurationId());
                            preparedStatement.setString(2, valueRecord.getKey());
                            preparedStatement.setString(3, value);
                        }

                        @Override
                        public int getBatchSize() {
                            return valueRecords.size();
                        }
                    }
            );
        }


        void update(final JobConfiguration jobConfiguration) {
            final List<ValueRecord> valueRecords = this.getValueRecords(jobConfiguration);
            final String sql = String.format(UPDATE_VALUES, this.tableName);
            this.jdbcTemplate.batchUpdate(
                    sql,
                    new BatchPreparedStatementSetter() {
                        @Override
                        public void setValues(final PreparedStatement preparedStatement, final int i) throws SQLException {
                            final ValueRecord valueRecord = valueRecords.get(i);
                            final String value = valueRecord.getValue() != null ? valueRecord.getValue().toString() : null;
                            preparedStatement.setString(1, value);
                            preparedStatement.setLong(2, valueRecord.getJobConfigurationId());
                            preparedStatement.setString(3, valueRecord.getKey());
                        }

                        @Override
                        public int getBatchSize() {
                            return valueRecords.size();
                        }
                    }
            );
        }

        void delete(final Long jobConfigurationId) {
            final String sql = String.format(DELETE_VALUES, this.tableName);
            this.jdbcTemplate.update(
                    sql,
                    new Object[]{
                            jobConfigurationId
                    },
                    new int[]{
                            Types.NUMERIC
                    }
            );
        }

        private List<ValueRecord> getValueRecords(final JobConfiguration jobConfiguration) {
            final Integer jobConfigurationType = JobConfigurationType.determineJobConfigurationType(jobConfiguration);
            final List<ValueRecord> valueRecords;
            if (JobConfigurationType.SCHEDULER.equals(jobConfigurationType)) {
                valueRecords = JobSchedulerConfigurationMapper.map(jobConfiguration.getJobSchedulerConfiguration(), jobConfiguration.getJobConfigurationId());
            } else if (JobConfigurationType.LISTENER.equals(jobConfigurationType)) {
                valueRecords = JobListenerConfigurationMapper.map(jobConfiguration.getJobListenerConfiguration(), jobConfiguration.getJobConfigurationId());
            } else {
                throw new SpringBatchLightminApplicationException("Unknown JobConfigurationType : " + jobConfigurationType);
            }
            return valueRecords;
        }

        private void attachByConfigurationType(final JobConfiguration jobConfiguration, final Integer
                configurationType, final List<ValueRecord> valueRecords) {
            final Map<String, Object> values = this.valueRecordRowMapper.map(valueRecords);
            if (JobConfigurationType.SCHEDULER.equals(configurationType)) {
                final JobSchedulerConfiguration jobSchedulerConfiguration = JobSchedulerConfigurationMapper.map(values);
                jobConfiguration.setJobSchedulerConfiguration(jobSchedulerConfiguration);
            } else if (JobConfigurationType.LISTENER.equals(configurationType)) {
                final JobListenerConfiguration jobListenerConfiguration = JobListenerConfigurationMapper.map(values);
                jobConfiguration.setJobListenerConfiguration(jobListenerConfiguration);
            } else {
                throw new SpringBatchLightminApplicationException("Unknown jobConfigurationType :" + configurationType);
            }
        }

        private static final class ValueRecordDomain {
            private ValueRecordDomain() {
            }

            static final String ID = "id";
            static final String JOB_CONFIGURATION_ID = "job_configuration_id";
            static final String KEY = "value_key";
            static final String VALUE = "configuration_value";
        }

        private static class ValueRecordRowMapper implements RowMapper<ValueRecord> {

            @Override
            public ValueRecord mapRow(final ResultSet resultSet, final int i) throws SQLException {
                final ValueRecord valueRecord = new ValueRecord();
                valueRecord.setId(resultSet.getLong(ValueRecordDomain.ID));
                valueRecord.setJobConfigurationId(resultSet.getLong(ValueRecordDomain.JOB_CONFIGURATION_ID));
                valueRecord.setKey(resultSet.getString(ValueRecordDomain.KEY));
                valueRecord.setValue(resultSet.getString(ValueRecordDomain.VALUE));
                return valueRecord;
            }

            public Map<String, Object> map(final List<ValueRecord> valueRecords) {
                final Map<String, Object> valueRecordMap = new HashMap<>();
                if (valueRecords != null) {
                    for (final ValueRecord valueRecord : valueRecords) {
                        valueRecordMap.put(valueRecord.getKey(), valueRecord.getValue());
                    }
                }
                return valueRecordMap;
            }
        }

        private static final class JobSchedulerConfigurationMapper {

            private JobSchedulerConfigurationMapper() {
            }

            public static JobSchedulerConfiguration map(final Map<String, Object> values) {
                final JobSchedulerConfiguration jobSchedulerConfiguration = new JobSchedulerConfiguration();
                jobSchedulerConfiguration.setBeanName(getValueOrNull(values, JobSchedulerConfigurationKey.BEAN_NAME, String.class));
                final JobSchedulerType jobSchedulerType = JobSchedulerType.getById(getValueOrNull(values, JobSchedulerConfigurationKey.SCHEDULER_TYPE, Long.class));
                if (JobSchedulerType.CRON == jobSchedulerType) {
                    jobSchedulerConfiguration.setCronExpression(getValueOrNull(values, JobSchedulerConfigurationKey.CRON_EXPRESSION, String.class));
                } else if (JobSchedulerType.PERIOD == jobSchedulerType) {
                    jobSchedulerConfiguration.setFixedDelay(getValueOrNull(values, JobSchedulerConfigurationKey.FIXED_DELAY, Long.class));
                    jobSchedulerConfiguration.setInitialDelay(getValueOrNull(values, JobSchedulerConfigurationKey.INITIAL_DELAY, Long.class));
                }
                jobSchedulerConfiguration.setJobSchedulerType(jobSchedulerType);
                final SchedulerStatus schedulerStatus = SchedulerStatus.getByValue(getValueOrNull(values, JobSchedulerConfigurationKey.STATUS, String.class));
                jobSchedulerConfiguration.setSchedulerStatus(schedulerStatus);
                final TaskExecutorType taskExecutorType = TaskExecutorType.getById(getValueOrNull(values, JobSchedulerConfigurationKey.TASK_EXECUTOR_TYPE, Long.class));
                jobSchedulerConfiguration.setTaskExecutorType(taskExecutorType);
                return jobSchedulerConfiguration;
            }

            public static List<ValueRecord> map(final JobSchedulerConfiguration jobSchedulerConfiguration,
                                                final Long jobConfigurationId) {
                final List<ValueRecord> valueRecords = new ArrayList<>();
                final ValueRecord beanName = new ValueRecord(jobConfigurationId, JobSchedulerConfigurationKey.BEAN_NAME, jobSchedulerConfiguration.getBeanName());
                final ValueRecord cronExpression = new ValueRecord(jobConfigurationId, JobSchedulerConfigurationKey.CRON_EXPRESSION, jobSchedulerConfiguration.getCronExpression());
                final ValueRecord fixedDelay = new ValueRecord(jobConfigurationId, JobSchedulerConfigurationKey.FIXED_DELAY, jobSchedulerConfiguration.getFixedDelay());
                final ValueRecord initialDelay = new ValueRecord(jobConfigurationId, JobSchedulerConfigurationKey.INITIAL_DELAY, jobSchedulerConfiguration.getInitialDelay());
                final ValueRecord schedulerStatus = new ValueRecord(jobConfigurationId, JobSchedulerConfigurationKey.STATUS, jobSchedulerConfiguration.getSchedulerStatus().getValue());
                final ValueRecord schedulerType = new ValueRecord(jobConfigurationId, JobSchedulerConfigurationKey.SCHEDULER_TYPE, jobSchedulerConfiguration.getJobSchedulerType().getId());
                final ValueRecord executorType = new ValueRecord(jobConfigurationId, JobSchedulerConfigurationKey.TASK_EXECUTOR_TYPE, jobSchedulerConfiguration.getTaskExecutorType().getId());
                valueRecords.add(beanName);
                valueRecords.add(cronExpression);
                valueRecords.add(fixedDelay);
                valueRecords.add(initialDelay);
                valueRecords.add(schedulerStatus);
                valueRecords.add(schedulerType);
                valueRecords.add(executorType);
                return valueRecords;
            }
        }

        private static final class JobListenerConfigurationMapper {
            private JobListenerConfigurationMapper() {
            }

            public static JobListenerConfiguration map(final Map<String, Object> values) {
                final JobListenerConfiguration jobListenerConfiguration = new JobListenerConfiguration();
                jobListenerConfiguration.setBeanName(getValueOrNull(values, JobListenerConfigurationKey.BEAN_NAME, String.class));
                jobListenerConfiguration.setFilePattern(getValueOrNull(values, JobListenerConfigurationKey.FILE_PATTERN, String.class));
                jobListenerConfiguration.setPollerPeriod(getValueOrNull(values, JobListenerConfigurationKey.POLLER_PERIOD, Long.class));
                jobListenerConfiguration.setSourceFolder(getValueOrNull(values, JobListenerConfigurationKey.SOURCE_FOLDER, String.class));
                final ListenerStatus listenerStatus = ListenerStatus.getByValue(getValueOrNull(values, JobListenerConfigurationKey.STATUS, String.class));
                jobListenerConfiguration.setListenerStatus(listenerStatus);
                final JobListenerType jobListenerType = JobListenerType.getById(getValueOrNull(values, JobListenerConfigurationKey.LISTENER_TYPE, Long.class));
                jobListenerConfiguration.setJobListenerType(jobListenerType);
                final TaskExecutorType taskExecutorType = TaskExecutorType.getById(getValueOrNull(values, JobSchedulerConfigurationKey.TASK_EXECUTOR_TYPE, Long.class));
                jobListenerConfiguration.setTaskExecutorType(taskExecutorType);
                return jobListenerConfiguration;
            }

            public static List<ValueRecord> map(final JobListenerConfiguration jobListenerConfiguration,
                                                final Long jobConfigurationId) {
                final List<ValueRecord> valueRecords = new ArrayList<>();
                final ValueRecord beanName = new ValueRecord(jobConfigurationId, JobListenerConfigurationKey.BEAN_NAME, jobListenerConfiguration.getBeanName());
                final ValueRecord filePattern = new ValueRecord(jobConfigurationId, JobListenerConfigurationKey.FILE_PATTERN, jobListenerConfiguration.getFilePattern());
                final ValueRecord pollerPeriod = new ValueRecord(jobConfigurationId, JobListenerConfigurationKey.POLLER_PERIOD, jobListenerConfiguration.getPollerPeriod());
                final ValueRecord sourceFolder = new ValueRecord(jobConfigurationId, JobListenerConfigurationKey.SOURCE_FOLDER, jobListenerConfiguration.getSourceFolder());
                final ValueRecord listenerStatus = new ValueRecord(jobConfigurationId, JobListenerConfigurationKey.STATUS, jobListenerConfiguration.getListenerStatus().getValue());
                final ValueRecord listenerType = new ValueRecord(jobConfigurationId, JobListenerConfigurationKey.LISTENER_TYPE, jobListenerConfiguration.getJobListenerType().getId());
                final ValueRecord executorType = new ValueRecord(jobConfigurationId, JobListenerConfigurationKey.TASK_EXECUTOR_TYPE, jobListenerConfiguration.getTaskExecutorType().getId());
                valueRecords.add(beanName);
                valueRecords.add(filePattern);
                valueRecords.add(pollerPeriod);
                valueRecords.add(sourceFolder);
                valueRecords.add(listenerStatus);
                valueRecords.add(listenerType);
                valueRecords.add(executorType);
                return valueRecords;
            }
        }


        @SuppressWarnings("unchecked")
        private static <T> T getValueOrNull(final Map<String, Object> map, final String key, final Class<T> clazz) {
            final T value;
            if (map.containsKey(key)) {
                final String mapValue = (String) map.get(key);
                if (clazz.isAssignableFrom(Long.class)) {
                    value = (T) new Long(Long.parseLong(mapValue));
                } else if (clazz.isAssignableFrom(String.class)) {
                    value = (T) mapValue;
                } else if (clazz.isAssignableFrom(Integer.class)) {
                    value = (T) new Integer(Integer.parseInt(mapValue));
                } else if (clazz.isAssignableFrom(Date.class)) {
                    value = (T) DomainParameterParser.parseDate(mapValue);
                } else if (clazz.isAssignableFrom(Double.class)) {
                    value = (T) new Double(Double.parseDouble(mapValue));
                } else {
                    value = (T) mapValue;
                }
            } else {
                value = null;
            }
            return value;
        }

        @Data
        private static class ValueRecord {
            private Long id;
            private Long jobConfigurationId;
            private String key;
            private Object value;

            ValueRecord() {
            }

            ValueRecord(final Long jobConfigurationId,
                        final String key,
                        final Object value) {
                this.jobConfigurationId = jobConfigurationId;
                this.key = key;
                this.value = value;
            }
        }

        private final class JobSchedulerConfigurationKey {

            private JobSchedulerConfigurationKey() {
            }

            static final String SCHEDULER_TYPE = "scheduler_type";
            static final String CRON_EXPRESSION = "cron_expression";
            static final String INITIAL_DELAY = "initial_delay";
            static final String FIXED_DELAY = "fixed_delay";
            static final String TASK_EXECUTOR_TYPE = "task_executor_type";
            static final String BEAN_NAME = "bean_name";
            static final String STATUS = "status";

        }

        private final class JobListenerConfigurationKey {

            private JobListenerConfigurationKey() {
            }

            static final String LISTENER_TYPE = "listener_type";
            static final String SOURCE_FOLDER = "source_folder";
            static final String FILE_PATTERN = "file_pattern";
            static final String POLLER_PERIOD = "poller_period";
            static final String BEAN_NAME = "bean_name";
            static final String TASK_EXECUTOR_TYPE = "task_executor_type";
            static final String STATUS = "status";
        }

    }


    /**
     *
     */
    private static class JobConfigurationParameterDAO {

        private static final String TABLE_NAME = "%s";

        private static final String GET_JOB_PARAMETERS_QUERY = "SELECT * FROM " + TABLE_NAME + " WHERE "
                + JobConfigurationParameterDomain.JOB_CONFIGURATION_ID + " = ?";

        private static final String DELETE_STATEMENT = "DELETE FROM " + TABLE_NAME + " WHERE "
                + JobConfigurationParameterDomain.JOB_CONFIGURATION_ID + " = ? ";

        private final JdbcTemplate jdbcTemplate;
        private final SimpleJdbcInsert simpleJdbcInsert;
        private final String tableName;
        private final DateFormat dateFormat;

        JobConfigurationParameterDAO(final JdbcTemplate jdbcTemplate, final String tableName, final String schema) {
            this.jdbcTemplate = jdbcTemplate;
            this.tableName = tableName;
            this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                    .withSchemaName(schema)
                    .withTableName(String.format(TABLE_NAME, tableName))
                    .usingGeneratedKeyColumns(JobConfigurationParameterDomain.ID);
            this.dateFormat = new SimpleDateFormat(DomainParameterParser.DATE_FORMAT_WITH_TIMESTAMP);
        }

        public void add(final JobConfiguration jobConfiguration) {
            final Long jobConfigurationId = jobConfiguration.getJobConfigurationId();
            final Map<String, Object> jobParameters = jobConfiguration.getJobParameters();
            if (jobParameters != null) {
                for (final Map.Entry<String, Object> jobParameter : jobParameters.entrySet()) {
                    final JobConfigurationParameter jobConfigurationParameter = this.createJobConfigurationParameter(
                            jobParameter.getKey(), jobParameter.getValue());
                    final String key = jobConfigurationParameter.getParameterName();
                    final String value = jobConfigurationParameter.getParameterValue();
                    final Long clazzType = jobConfigurationParameter.getParameterType();
                    final Map<String, Object> parameters = new HashMap<>();
                    parameters.put(JobConfigurationParameterDomain.JOB_CONFIGURATION_ID, jobConfigurationId);
                    parameters.put(JobConfigurationParameterDomain.PARAMETER_NAME, key);
                    parameters.put(JobConfigurationParameterDomain.PARAMETER_TYPE, clazzType);
                    parameters.put(JobConfigurationParameterDomain.PARAMETER_VALUE, value);
                    this.simpleJdbcInsert.executeAndReturnKey(parameters);
                }
            } else {
                log.info("JobParameters null, nothing to map!");
            }
        }

        void attachParameters(final JobConfiguration jobConfiguration) {
            final Long jobConfigurationId = jobConfiguration.getJobConfigurationId();
            final String sql = String.format(GET_JOB_PARAMETERS_QUERY, this.tableName);
            final List<JobConfigurationParameter> jobConfigurationParameters = this.jdbcTemplate.query(sql,
                    new JobConfigurationParameterRowMapper(), jobConfigurationId);
            final Map<String, Object> jobParameters = new HashMap<>();
            for (final JobConfigurationParameter jobConfigurationParameter : jobConfigurationParameters) {
                final String key = jobConfigurationParameter.getParameterName();
                final Long typeId = jobConfigurationParameter.getParameterType();
                final String valueString = jobConfigurationParameter.getParameterValue();
                final ParameterType parameterType = ParameterType.getById(typeId);
                final Object value = this.createValue(valueString, parameterType);
                jobParameters.put(key, value);
            }
            jobConfiguration.setJobParameters(jobParameters);
        }

        public void delete(final Long jobConfigurationId) {
            final String sql = String.format(DELETE_STATEMENT, this.tableName);
            this.jdbcTemplate.update(sql, new Object[]{jobConfigurationId}, new int[]{Types.NUMERIC});
        }

        private Object createValue(final String value, final ParameterType parameterType) {
            if (ParameterType.LONG.equals(parameterType)) {
                return Long.parseLong(value);
            } else if (ParameterType.STRING.equals(parameterType)) {
                return value;
            } else if (ParameterType.DOUBLE.equals(parameterType)) {
                return Double.parseDouble(value);
            } else if (ParameterType.DATE.equals(parameterType)) {
                try {
                    return this.dateFormat.parse(value);
                } catch (final ParseException e) {
                    throw new SpringBatchLightminApplicationException(e, e.getMessage());
                }
            } else {
                throw new SpringBatchLightminApplicationException("Unsupported ParameterType: "
                        + parameterType.getClazz().getSimpleName());
            }
        }

        private JobConfigurationParameter createJobConfigurationParameter(final String key, final Object value) {
            final JobConfigurationParameter jobConfigurationParameter = new JobConfigurationParameter();
            if (value instanceof Long || value instanceof Integer) {
                jobConfigurationParameter.setParameterValue(value.toString());
                jobConfigurationParameter.setParameterType(ParameterType.LONG.getId());
            } else if (value instanceof String) {
                jobConfigurationParameter.setParameterValue(value.toString());
                jobConfigurationParameter.setParameterType(ParameterType.STRING.getId());
            } else if (value instanceof Date) {
                jobConfigurationParameter.setParameterValue(this.dateFormat.format(value));
                jobConfigurationParameter.setParameterType(ParameterType.DATE.getId());
            } else if (value instanceof Double) {
                jobConfigurationParameter.setParameterValue(value.toString());
                jobConfigurationParameter.setParameterType(ParameterType.DOUBLE.getId());
            } else {
                throw new SpringBatchLightminApplicationException("Unknown jobParameterType: "
                        + value.getClass().getSimpleName());
            }
            jobConfigurationParameter.setParameterName(key);
            return jobConfigurationParameter;
        }

        private static class JobConfigurationParameterRowMapper implements RowMapper<JobConfigurationParameter> {

            @Override
            public JobConfigurationParameter mapRow(final ResultSet resultSet, final int rowNum) throws SQLException {
                final JobConfigurationParameter jobConfigurationParameter = new JobConfigurationParameter();
                jobConfigurationParameter.setParameterName(resultSet
                        .getString(JobConfigurationParameterDomain.PARAMETER_NAME));
                jobConfigurationParameter.setParameterValue(resultSet
                        .getString(JobConfigurationParameterDomain.PARAMETER_VALUE));
                jobConfigurationParameter.setParameterType(resultSet
                        .getLong(JobConfigurationParameterDomain.PARAMETER_TYPE));
                return jobConfigurationParameter;
            }
        }

        @Data
        private static class JobConfigurationParameter {
            private String parameterName;
            private String parameterValue;
            private Long parameterType;

        }

        private static final class JobConfigurationParameterDomain {

            private JobConfigurationParameterDomain() {
            }

            static final String ID = "id";
            static final String JOB_CONFIGURATION_ID = "job_configuration_id";
            static final String PARAMETER_NAME = "parameter_name";
            static final String PARAMETER_VALUE = "parameter_value";
            static final String PARAMETER_TYPE = "parameter_type";
        }
    }
}
