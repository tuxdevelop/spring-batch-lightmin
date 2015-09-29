package org.tuxdevelop.spring.batch.lightmin.admin.repository;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.repository.dao.AbstractJdbcBatchMetadataDao;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.*;
import org.tuxdevelop.spring.batch.lightmin.exception.NoSuchJobConfigurationException;
import org.tuxdevelop.spring.batch.lightmin.exception.NoSuchJobException;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminApplicationException;
import org.tuxdevelop.spring.batch.lightmin.util.ParameterParser;

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
    private final String tablePrefix;
    private final JobConfigurationDAO jobConfigurationDAO;
    private final JobSchedulerConfigurationDAO jobSchedulerConfigurationDAO;
    private final JobConfigurationParameterDAO jobConfigurationParameterDAO;

    public JdbcJobConfigurationRepository(final JdbcTemplate jdbcTemplate, final String tablePrefix) {
        this.jdbcTemplate = jdbcTemplate;
        if (tablePrefix != null) {
            this.tablePrefix = tablePrefix;
        } else {
            this.tablePrefix = AbstractJdbcBatchMetadataDao.DEFAULT_TABLE_PREFIX;
        }
        this.jobSchedulerConfigurationDAO = new JobSchedulerConfigurationDAO(jdbcTemplate, tablePrefix);
        this.jobConfigurationDAO = new JobConfigurationDAO(jdbcTemplate, tablePrefix);
        this.jobConfigurationParameterDAO = new JobConfigurationParameterDAO(jdbcTemplate, tablePrefix);
    }

    @Override
    public JobConfiguration getJobConfiguration(final Long jobConfigurationId) throws NoSuchJobConfigurationException {
        if (checkJobConfigurationExists(jobConfigurationId)) {
            final JobConfiguration jobConfiguration = jobConfigurationDAO.getById(jobConfigurationId);
            jobSchedulerConfigurationDAO.attacheJobSchedulerConfiguration(jobConfiguration);
            jobConfigurationParameterDAO.attacheParameters(jobConfiguration);
            return jobConfiguration;
        } else {
            final String message = "No jobConfiguration could be found for id:" + jobConfigurationId;
            log.error(message);
            throw new NoSuchJobConfigurationException(message);
        }
    }

    @Override
    public Collection<JobConfiguration> getJobConfigurations(final String jobName) throws NoSuchJobException {
        if (checkJobConfigurationExists(jobName)) {
            final List<JobConfiguration> jobConfigurations = jobConfigurationDAO.getByJobName(jobName);
            for (final JobConfiguration jobConfiguration : jobConfigurations) {
                jobSchedulerConfigurationDAO.attacheJobSchedulerConfiguration(jobConfiguration);
                jobConfigurationParameterDAO.attacheParameters(jobConfiguration);
            }
            return jobConfigurations;
        } else {
            final String message = "No jobConfiguration could be found for jobName:" + jobName;
            log.error(message);
            throw new NoSuchJobException(message);
        }
    }

    @Override
    public JobConfiguration add(final JobConfiguration jobConfiguration) {
        final Long jobConfigurationId = jobConfigurationDAO.add(jobConfiguration);
        jobConfiguration.setJobConfigurationId(jobConfigurationId);
        jobSchedulerConfigurationDAO.add(jobConfiguration);
        jobConfigurationParameterDAO.add(jobConfiguration);
        return jobConfiguration;
    }

    @Override
    public JobConfiguration update(final JobConfiguration jobConfiguration) throws NoSuchJobConfigurationException {
        final Long jobConfigurationId = jobConfiguration.getJobConfigurationId();
        if (checkJobConfigurationExists(jobConfigurationId)) {
            jobConfigurationDAO.update(jobConfiguration);
            jobSchedulerConfigurationDAO.update(jobConfiguration);
            jobConfigurationParameterDAO.delete(jobConfigurationId);
            jobConfigurationParameterDAO.add(jobConfiguration);
            return jobConfiguration;
        } else {
            final String message = "No jobConfiguration could be found for id:" + jobConfiguration;
            log.error(message);
            throw new NoSuchJobConfigurationException(message);
        }
    }

    @Override
    public void delete(final JobConfiguration jobConfiguration) throws NoSuchJobConfigurationException {
        final Long jobConfigurationId = jobConfiguration.getJobConfigurationId();
        if (checkJobConfigurationExists(jobConfigurationId)) {
            jobConfigurationParameterDAO.delete(jobConfigurationId);
            jobSchedulerConfigurationDAO.delete(jobConfigurationId);
            jobConfigurationDAO.delete(jobConfigurationId);
        } else {
            final String message = "No jobConfiguration could be found for id:" + jobConfiguration;
            log.error(message);
            throw new NoSuchJobConfigurationException(message);
        }
    }

    @Override
    public Collection<JobConfiguration> getAllJobConfigurations() {
        final List<JobConfiguration> jobConfigurations = jobConfigurationDAO.getAll();
        for (final JobConfiguration jobConfiguration : jobConfigurations) {
            jobSchedulerConfigurationDAO.attacheJobSchedulerConfiguration(jobConfiguration);
            jobConfigurationParameterDAO.attacheParameters(jobConfiguration);
        }
        return jobConfigurations;
    }

    @Override
    public Collection<JobConfiguration> getAllJobConfigurationsByJobNames(final Collection<String> jobNames) {
        final List<JobConfiguration> jobConfigurations = jobConfigurationDAO.getAllByJobNames(jobNames);
        for (final JobConfiguration jobConfiguration : jobConfigurations) {
            jobSchedulerConfigurationDAO.attacheJobSchedulerConfiguration(jobConfiguration);
            jobConfigurationParameterDAO.attacheParameters(jobConfiguration);
        }
        return jobConfigurations;
    }

    @Override
    public void afterPropertiesSet() {
        assert jdbcTemplate != null;
        assert tablePrefix != null;
    }

	/*
     * -------------------------- HELPER CLASSES AND METHODS
	 * --------------------------
	 */

    private Boolean checkJobConfigurationExists(final Long jobConfigurationId) {
        return jobConfigurationDAO.getJobConfigurationIdCount(jobConfigurationId) > 0;
    }

    private Boolean checkJobConfigurationExists(final String jobName) {
        return jobConfigurationDAO.getJobNameCount(jobName) > 0;
    }

    /**
     *
     */
    private static class JobConfigurationDAO {

        private static final String TABLE_NAME = "%sJOB_CONFIGURATION";

        private static final String GET_JOB_CONFIGURATION_QUERY = "SELECT * FROM " + TABLE_NAME + " WHERE "
                + JobConfigurationDomain.JOB_CONFIGURATION_ID + " = ?";

        private static final String GET_JOB_CONFIGURATIONS_BY_JOB_NAME_QUERY = "SELECT * FROM " + TABLE_NAME
                + " WHERE " + JobConfigurationDomain.JOB_NAME + " = ?";

        private static final String UPDATE_STATEMENT = "UPDATE " + TABLE_NAME + " SET "
                + JobConfigurationDomain.JOB_NAME + "" + " = ? , " + JobConfigurationDomain.JOB_INCREMENTER
                + " = ? WHERE " + JobConfigurationDomain.JOB_CONFIGURATION_ID + " = ?";

        private static final String DELETE_STATEMENT = "DELETE FROM " + TABLE_NAME + " WHERE "
                + JobConfigurationDomain.JOB_CONFIGURATION_ID + " = ?";

        private static final String GET_JOB_CONFIGURATION_ID_COUNT_STATEMENT = "SELECT COUNT(1) FROM " + TABLE_NAME
                + " WHERE" + " " + JobConfigurationDomain.JOB_CONFIGURATION_ID + " = ?";

        private static final String GET_JOB_NAME_COUNT_STATEMENT = "SELECT COUNT(1) FROM " + TABLE_NAME + " WHERE"
                + " " + JobConfigurationDomain.JOB_NAME + " = ?";

        private static final String GET_ALL_JOB_CONFIGURATION_QUERY = "SELECT * FROM " + TABLE_NAME;

        private static final String GET_ALL_JOB_CONFIGURATION_BY_JOB_NAMES_QUERY = "SELECT * FROM " + TABLE_NAME + " " +
                "WHERE " + JobConfigurationDomain.JOB_NAME + " IN (%s)";

        private final JdbcTemplate jdbcTemplate;
        private final SimpleJdbcInsert simpleJdbcInsert;
        private final String tablePrefix;

        public JobConfigurationDAO(final JdbcTemplate jdbcTemplate, final String tablePrefix) {
            this.jdbcTemplate = jdbcTemplate;
            this.tablePrefix = tablePrefix;
            this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName(
                    String.format(TABLE_NAME, tablePrefix)).usingGeneratedKeyColumns(
                    JobConfigurationDomain.JOB_CONFIGURATION_ID);
        }

        public Long add(final JobConfiguration jobConfiguration) {
            final Map<String, ?> keyValues = map(jobConfiguration);
            final Number key = simpleJdbcInsert.executeAndReturnKey(keyValues);
            return key.longValue();
        }

        public JobConfiguration getById(final Long jobConfigurationId) {
            final String sql = String.format(GET_JOB_CONFIGURATION_QUERY, tablePrefix);
            return jdbcTemplate.queryForObject(sql, new JobConfigurationRowMapper(), jobConfigurationId);
        }

        public List<JobConfiguration> getByJobName(final String jobName) {
            final String sql = String.format(GET_JOB_CONFIGURATIONS_BY_JOB_NAME_QUERY, tablePrefix);
            return jdbcTemplate.query(sql, new JobConfigurationRowMapper(), jobName);
        }

        public void update(final JobConfiguration jobConfiguration) {
            final String sql = String.format(UPDATE_STATEMENT, tablePrefix);
            jdbcTemplate.update(
                    sql,
                    new Object[]{jobConfiguration.getJobName(),
                            jobConfiguration.getJobIncrementer().getIncrementerIdentifier(),
                            jobConfiguration.getJobConfigurationId()}, new int[]{Types.VARCHAR, Types.VARCHAR,
                            Types.NUMERIC});
        }

        public void delete(final Long jobConfigurationId) {
            final String sql = String.format(DELETE_STATEMENT, tablePrefix);
            jdbcTemplate.update(sql, new Object[]{jobConfigurationId}, new int[]{Types.NUMERIC});
        }

        public Long getJobConfigurationIdCount(final Long jobConfiguration) {
            final String sql = String.format(GET_JOB_CONFIGURATION_ID_COUNT_STATEMENT, tablePrefix);
            return jdbcTemplate.queryForObject(sql, new Object[]{jobConfiguration}, new int[]{Types.NUMERIC},
                    Long.class);
        }

        public Long getJobNameCount(final String jobName) {
            final String sql = String.format(GET_JOB_NAME_COUNT_STATEMENT, tablePrefix);
            return jdbcTemplate.queryForObject(sql, new Object[]{jobName}, new int[]{Types.VARCHAR}, Long.class);
        }

        public List<JobConfiguration> getAll() {
            final String sql = String.format(GET_ALL_JOB_CONFIGURATION_QUERY, tablePrefix);
            return jdbcTemplate.query(sql, new JobConfigurationRowMapper());
        }

        public List<JobConfiguration> getAllByJobNames(final Collection<String> jobNames) {
            final String inParameters = parseInCollection(jobNames);
            final String sql = String.format(GET_ALL_JOB_CONFIGURATION_BY_JOB_NAMES_QUERY, tablePrefix, inParameters);
            return jdbcTemplate
                    .query(sql, new JobConfigurationRowMapper(), jobNames.toArray());
        }

        private Map<String, Object> map(final JobConfiguration jobConfiguration) {
            final Map<String, Object> keyValues = new HashMap<String, Object>();
            keyValues.put(JobConfigurationDomain.JOB_NAME, jobConfiguration.getJobName());
            keyValues.put(JobConfigurationDomain.JOB_INCREMENTER, jobConfiguration.getJobIncrementer()
                    .getIncrementerIdentifier());
            if (jobConfiguration.getJobConfigurationId() != null) {
                keyValues.put(JobConfigurationDomain.JOB_CONFIGURATION_ID, jobConfiguration.getJobConfigurationId());
            }
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
    }

    /**
     *
     */
    private static class JobSchedulerConfigurationDAO {

        private static final String TABLE_NAME = "%sJOB_SCHEDULER_CONFIGURATION";

        private static final String GET_JOB_SCHEDULER_QUERY = "SELECT * FROM " + TABLE_NAME + " WHERE "
                + JobSchedulerConfigurationDomain.JOB_CONFIGURATION_ID + " = ?";

        private static final String UPDATE_STATEMENT = "UPDATE " + TABLE_NAME + " SET "
                + JobSchedulerConfigurationDomain.CRON_EXPRESSION + " = ? , "
                + JobSchedulerConfigurationDomain.FIXED_DELAY + " = ? , "
                + JobSchedulerConfigurationDomain.INITIAL_DELAY + " = ? , "
                + JobSchedulerConfigurationDomain.SCHEDULER_TYPE + " = ?, "
                + JobSchedulerConfigurationDomain.TASK_EXECUTOR_TYPE + " = ?, "
                + JobSchedulerConfigurationDomain.BEAN_NAME + " = ?, "
                + JobSchedulerConfigurationDomain.STATUS + " = ? WHERE "
                + JobSchedulerConfigurationDomain.JOB_CONFIGURATION_ID + " = ? ";

        private static final String DELETE_STATEMENT = "DELETE FROM " + TABLE_NAME + " WHERE "
                + JobSchedulerConfigurationDomain.JOB_CONFIGURATION_ID + " = ?";

        private final JdbcTemplate jdbcTemplate;
        private final SimpleJdbcInsert simpleJdbcInsert;
        private final String tablePrefix;

        public JobSchedulerConfigurationDAO(final JdbcTemplate jdbcTemplate, final String tablePrefix) {
            this.jdbcTemplate = jdbcTemplate;
            this.tablePrefix = tablePrefix;
            this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName(
                    String.format(TABLE_NAME, tablePrefix))
                    .usingGeneratedKeyColumns(JobSchedulerConfigurationDomain.ID);
        }

        public Long add(final JobConfiguration jobConfiguration) {
            final Map<String, ?> keyValues = map(jobConfiguration);
            final Number key = simpleJdbcInsert.executeAndReturnKey(keyValues);
            return key.longValue();
        }

        public void attacheJobSchedulerConfiguration(final JobConfiguration jobConfiguration) {
            final String sql = String.format(GET_JOB_SCHEDULER_QUERY, tablePrefix);
            final JobSchedulerConfiguration jobSchedulerConfiguration = jdbcTemplate.queryForObject(sql,
                    new JobSchedulerConfigurationRowMapper(), jobConfiguration.getJobConfigurationId());
            jobConfiguration.setJobSchedulerConfiguration(jobSchedulerConfiguration);
        }

        public void update(final JobConfiguration jobConfiguration) {
            final JobSchedulerConfiguration jobSchedulerConfiguration = jobConfiguration.getJobSchedulerConfiguration();
            final String sql = String.format(UPDATE_STATEMENT, tablePrefix);
            final Object[] parameters = {
                    jobSchedulerConfiguration.getCronExpression(),
                    jobSchedulerConfiguration.getFixedDelay(),
                    jobSchedulerConfiguration.getInitialDelay(),
                    jobSchedulerConfiguration.getJobSchedulerType().getId(),
                    jobSchedulerConfiguration.getTaskExecutorType().getId(),
                    jobSchedulerConfiguration.getBeanName(),
                    jobSchedulerConfiguration.getSchedulerStatus().getValue(),
                    jobConfiguration.getJobConfigurationId()};
            final int[] types = {
                    Types.VARCHAR,
                    Types.NUMERIC,
                    Types.NUMERIC,
                    Types.NUMERIC,
                    Types.NUMERIC,
                    Types.VARCHAR,
                    Types.VARCHAR,
                    Types.NUMERIC};
            jdbcTemplate.update(sql, parameters, types);
        }

        public void delete(final Long jobConfigurationId) {
            final String sql = String.format(DELETE_STATEMENT, tablePrefix);
            jdbcTemplate.update(sql, new Object[]{jobConfigurationId}, new int[]{Types.NUMERIC});
        }

        private Map<String, Object> map(final JobConfiguration jobConfiguration) {
            final JobSchedulerConfiguration jobSchedulerConfiguration = jobConfiguration.getJobSchedulerConfiguration();
            final Map<String, Object> keyValues = new HashMap<String, Object>();
            keyValues.put(JobSchedulerConfigurationDomain.JOB_CONFIGURATION_ID,
                    jobConfiguration.getJobConfigurationId());
            keyValues.put(JobSchedulerConfigurationDomain.CRON_EXPRESSION,
                    jobSchedulerConfiguration.getCronExpression());
            keyValues.put(JobSchedulerConfigurationDomain.INITIAL_DELAY, jobSchedulerConfiguration.getInitialDelay());
            keyValues.put(JobSchedulerConfigurationDomain.FIXED_DELAY, jobSchedulerConfiguration.getFixedDelay());
            keyValues.put(JobSchedulerConfigurationDomain.SCHEDULER_TYPE, jobSchedulerConfiguration
                    .getJobSchedulerType().getId());
            keyValues.put(JobSchedulerConfigurationDomain.TASK_EXECUTOR_TYPE, jobSchedulerConfiguration
                    .getTaskExecutorType().getId());
            keyValues.put(JobSchedulerConfigurationDomain.BEAN_NAME, jobSchedulerConfiguration.getBeanName());
            keyValues.put(JobSchedulerConfigurationDomain.STATUS,
                    jobSchedulerConfiguration.getSchedulerStatus().getValue());
            return keyValues;
        }
    }

    /**
     *
     */
    private static class JobConfigurationParameterDAO {

        private static final String TABLE_NAME = "%sJOB_CONFIGURATION_PARAMETERS";

        private static final String GET_JOB_PARAMETERS_QUERY = "SELECT * FROM " + TABLE_NAME + " WHERE "
                + JobSchedulerConfigurationDomain.JOB_CONFIGURATION_ID + " = ?";

        private static final String DELETE_STATEMENT = "DELETE FROM " + TABLE_NAME + " WHERE "
                + JobConfigurationParameterDomain.JOB_CONFIGURATION_ID + " = ? ";

        private final JdbcTemplate jdbcTemplate;
        private final SimpleJdbcInsert simpleJdbcInsert;
        private final String tablePrefix;
        private final DateFormat dateFormat;

        public JobConfigurationParameterDAO(final JdbcTemplate jdbcTemplate, final String tablePrefix) {
            this.jdbcTemplate = jdbcTemplate;
            this.tablePrefix = tablePrefix;
            this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName(
                    String.format(TABLE_NAME, tablePrefix))
                    .usingGeneratedKeyColumns(JobConfigurationParameterDomain.ID);
            this.dateFormat = new SimpleDateFormat(ParameterParser.DATE_FORMAT_WITH_TIMESTAMP);
        }

        public void add(final JobConfiguration jobConfiguration) {
            final Long jobConfigurationId = jobConfiguration.getJobConfigurationId();
            final Map<String, Object> jobParameters = jobConfiguration.getJobParameters();
            for (final Map.Entry<String, Object> jobParameter : jobParameters.entrySet()) {
                final JobConfigurationParameter jobConfigurationParameter = createJobConfigurationParameter(
                        jobParameter.getKey(), jobParameter.getValue());
                final String key = jobConfigurationParameter.getParameterName();
                final String value = jobConfigurationParameter.getParameterValue();
                final Long clazzType = jobConfigurationParameter.getParameterType();
                final Map<String, Object> parameters = new HashMap<String, Object>();
                parameters.put(JobConfigurationParameterDomain.JOB_CONFIGURATION_ID, jobConfigurationId);
                parameters.put(JobConfigurationParameterDomain.PARAMETER_NAME, key);
                parameters.put(JobConfigurationParameterDomain.PARAMETER_TYPE, clazzType);
                parameters.put(JobConfigurationParameterDomain.PARAMETER_VALUE, value);
                simpleJdbcInsert.executeAndReturnKey(parameters);
            }
        }

        public void attacheParameters(final JobConfiguration jobConfiguration) {
            final Long jobConfigurationId = jobConfiguration.getJobConfigurationId();
            final String sql = String.format(GET_JOB_PARAMETERS_QUERY, tablePrefix);
            final List<JobConfigurationParameter> jobConfigurationParameters = jdbcTemplate.query(sql,
                    new JobConfigurationParameterRowMapper(), jobConfigurationId);
            final Map<String, Object> jobParameters = new HashMap<String, Object>();
            for (final JobConfigurationParameter jobConfigurationParameter : jobConfigurationParameters) {
                final String key = jobConfigurationParameter.getParameterName();
                final Long typeId = jobConfigurationParameter.getParameterType();
                final String valueString = jobConfigurationParameter.getParameterValue();
                final ParameterType parameterType = ParameterType.getById(typeId);
                final Object value = createValue(valueString, parameterType);
                jobParameters.put(key, value);
            }
            jobConfiguration.setJobParameters(jobParameters);
        }

        public void delete(final Long jobConfigurationId) {
            final String sql = String.format(DELETE_STATEMENT, tablePrefix);
            jdbcTemplate.update(sql, new Object[]{jobConfigurationId}, new int[]{Types.NUMERIC});
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

        private JobConfigurationParameter createJobConfigurationParameter(final String key, final Object o) {
            final JobConfigurationParameter jobConfigurationParameter = new JobConfigurationParameter();
            if (o instanceof Long) {
                jobConfigurationParameter.setParameterValue(o.toString());
                jobConfigurationParameter.setParameterType(ParameterType.LONG.getId());
            } else if (o instanceof String) {
                jobConfigurationParameter.setParameterValue(o.toString());
                jobConfigurationParameter.setParameterType(ParameterType.STRING.getId());
            } else if (o instanceof Date) {
                jobConfigurationParameter.setParameterValue(dateFormat
                        .format(ParameterParser.DATE_FORMAT_WITH_TIMESTAMP));
                jobConfigurationParameter.setParameterType(ParameterType.DATE.getId());
            } else if (o instanceof Double) {
                jobConfigurationParameter.setParameterValue(o.toString());
                jobConfigurationParameter.setParameterType(ParameterType.DOUBLE.getId());
            } else {
                throw new SpringBatchLightminApplicationException("Unknown jobParameterType: "
                        + o.getClass().getSimpleName());
            }
            jobConfigurationParameter.setParameterName(key);
            return jobConfigurationParameter;
        }
    }

    /**
     *
     */
    private static class JobSchedulerConfigurationRowMapper implements RowMapper<JobSchedulerConfiguration> {

        @Override
        public JobSchedulerConfiguration mapRow(final ResultSet resultSet, final int rowNum) throws SQLException {
            final JobSchedulerConfiguration jobSchedulerConfiguration = new JobSchedulerConfiguration();
            jobSchedulerConfiguration.setBeanName(resultSet.getString(JobSchedulerConfigurationDomain.BEAN_NAME));
            jobSchedulerConfiguration.setCronExpression(resultSet
                    .getString(JobSchedulerConfigurationDomain.CRON_EXPRESSION));
            jobSchedulerConfiguration.setFixedDelay(resultSet.getLong(JobSchedulerConfigurationDomain.FIXED_DELAY));
            jobSchedulerConfiguration.setInitialDelay(resultSet.getLong(JobSchedulerConfigurationDomain.INITIAL_DELAY));
            final JobSchedulerType jobSchedulerType = JobSchedulerType.getById(resultSet
                    .getLong(JobSchedulerConfigurationDomain.SCHEDULER_TYPE));
            jobSchedulerConfiguration.setJobSchedulerType(jobSchedulerType);
            final TaskExecutorType taskExecutorType = TaskExecutorType.getById(resultSet
                    .getLong(JobSchedulerConfigurationDomain.TASK_EXECUTOR_TYPE));
            jobSchedulerConfiguration.setTaskExecutorType(taskExecutorType);
            SchedulerStatus schedulerStatus = SchedulerStatus.getByValue(resultSet.getString
                    (JobSchedulerConfigurationDomain.STATUS));
            jobSchedulerConfiguration.setSchedulerStatus(schedulerStatus);
            return jobSchedulerConfiguration;
        }
    }

    /**
     *
     */
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

    /**
     *
     */
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
}
