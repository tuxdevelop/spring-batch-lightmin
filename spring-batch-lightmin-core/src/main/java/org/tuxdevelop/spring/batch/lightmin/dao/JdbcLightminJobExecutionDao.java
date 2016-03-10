package org.tuxdevelop.spring.batch.lightmin.dao;

import org.springframework.batch.core.*;
import org.springframework.batch.core.repository.dao.JdbcJobExecutionDao;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JdbcLightminJobExecutionDao extends JdbcJobExecutionDao implements LightminJobExecutionDao, InitializingBean {

    private static final String FIELDS = "E.JOB_EXECUTION_ID, E.START_TIME, E.END_TIME, E.STATUS, E.EXIT_CODE, E.EXIT_MESSAGE, "
            + "E.CREATE_TIME, E.LAST_UPDATED, E.VERSION, I.JOB_INSTANCE_ID, I.JOB_NAME";

    private final String GET_EXECUTION_COUNT = "SELECT " +
            "COUNT(*) " +
            "FROM %PREFIX%JOB_EXECUTION" +
            " WHERE JOB_INSTANCE_ID = ?";

    private PagingQueryProvider byJobNamePagingQueryProvider;
    private PagingQueryProvider byJobInstanceIdExecutionsPagingQueryProvider;

    private final DataSource dataSource;

    public JdbcLightminJobExecutionDao(final DataSource dataSource) throws Exception {
        this.dataSource = dataSource;
    }

    @Override
    public List<JobExecution> findJobExecutions(final JobInstance jobInstance, final int start, final int count) {
        if (start <= 0) {
            return getJdbcTemplate().query(byJobInstanceIdExecutionsPagingQueryProvider.generateFirstPageQuery(count),
                    new JobExecutionRowMapper(jobInstance), jobInstance.getInstanceId());
        }
        try {
            final Long startAfterValue = getJdbcTemplate().queryForObject(
                    byJobInstanceIdExecutionsPagingQueryProvider.generateJumpToItemQuery(start, count), Long.class,
                    jobInstance.getInstanceId());
            return getJdbcTemplate().query(byJobInstanceIdExecutionsPagingQueryProvider.generateRemainingPagesQuery(count),
                    new JobExecutionRowMapper(jobInstance), jobInstance.getInstanceId(), startAfterValue);
        } catch (final IncorrectResultSizeDataAccessException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public int getJobExecutionCount(final JobInstance jobInstance) {
        return getJdbcTemplate().queryForObject(getQuery(GET_EXECUTION_COUNT), Integer.class,
                new Object[]{jobInstance.getInstanceId()});
    }

    @Override
    public List<JobExecution> getJobExecutions(final String jobName, final int start, final int count) {
        if (start <= 0) {
            return getJdbcTemplate().query(byJobNamePagingQueryProvider.generateFirstPageQuery(count),
                    new JobExecutionRowMapper(), jobName);
        }
        try {
            final Long startAfterValue = getJdbcTemplate().queryForObject(
                    byJobNamePagingQueryProvider.generateJumpToItemQuery(start, count), Long.class, jobName);
            return getJdbcTemplate().query(byJobNamePagingQueryProvider.generateRemainingPagesQuery(count),
                    new JobExecutionRowMapper(), jobName, startAfterValue);
        } catch (final IncorrectResultSizeDataAccessException e) {
            return Collections.emptyList();
        }
    }

    /**
     * Ported from
     * {@link org.springframework.batch.core.repository.dao.JdbcJobExecutionDao}
     */
    private final class JobExecutionRowMapper implements RowMapper<JobExecution> {

        private final JobInstance jobInstance;
        private JobParameters jobParameters;

        public JobExecutionRowMapper() {
            this(null);
        }

        public JobExecutionRowMapper(final JobInstance jobInstance) {
            this.jobInstance = jobInstance;
        }

        @Override
        public JobExecution mapRow(final ResultSet resultSet, final int rowNumber) throws SQLException {
            final Long id = Long.valueOf(resultSet.getLong(1));
            final String jobConfigurationLocation = resultSet.getString(10);
            if (jobParameters == null) {
                jobParameters = getJobParameters(id);
            }

            final JobExecution jobExecution;
            if (jobInstance == null) {
                jobExecution = new JobExecution(id, jobParameters, jobConfigurationLocation);
            } else {
                jobExecution = new JobExecution(jobInstance, id, jobParameters, jobConfigurationLocation);
            }

            jobExecution.setStartTime(resultSet.getTimestamp(2));
            jobExecution.setEndTime(resultSet.getTimestamp(3));
            jobExecution.setStatus(BatchStatus.valueOf(resultSet.getString(4)));
            jobExecution.setExitStatus(new ExitStatus(resultSet.getString(5), resultSet.getString(6)));
            jobExecution.setCreateTime(resultSet.getTimestamp(7));
            jobExecution.setLastUpdated(resultSet.getTimestamp(8));
            jobExecution.setVersion(Integer.valueOf(resultSet.getInt(9)));
            return jobExecution;
        }
    }

    /**
     * Ported From Spring Batch Admin Searchable JdbcSearchableJobExecutionDao
     */

    /**
     * @return a {@link PagingQueryProvider} for all job executions
     * @throws Exception
     */
    private PagingQueryProvider getPagingQueryProvider() throws Exception {
        return getPagingQueryProvider(null);
    }

    /**
     * @return a {@link PagingQueryProvider} for all job executions with the provided where clause
     * @throws Exception
     */
    private PagingQueryProvider getPagingQueryProvider(final String whereClause) throws Exception {
        return getPagingQueryProvider(null, whereClause);
    }

    /**
     * @return a {@link PagingQueryProvider} with a where clause to narrow the query
     * @throws Exception
     */
    private PagingQueryProvider getPagingQueryProvider(String fromClause, String whereClause) throws Exception {
        final SqlPagingQueryProviderFactoryBean factory = new SqlPagingQueryProviderFactoryBean();
        factory.setDataSource(dataSource);
        fromClause = "%PREFIX%JOB_EXECUTION E, %PREFIX%JOB_INSTANCE I" + (fromClause == null ? "" : ", " + fromClause);
        factory.setFromClause(getQuery(fromClause));
        factory.setSelectClause(FIELDS);
        final Map<String, Order> sortKeys = new HashMap<String, Order>();
        sortKeys.put("JOB_EXECUTION_ID", Order.DESCENDING);
        factory.setSortKeys(sortKeys);
        whereClause = "E.JOB_INSTANCE_ID=I.JOB_INSTANCE_ID" + (whereClause == null ? "" : " and " + whereClause);
        factory.setWhereClause(whereClause);

        return factory.getObject();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        this.byJobNamePagingQueryProvider = getPagingQueryProvider("I.JOB_NAME=?");
        this.byJobInstanceIdExecutionsPagingQueryProvider = getPagingQueryProvider("I.JOB_INSTANCE_ID=?");
    }
}