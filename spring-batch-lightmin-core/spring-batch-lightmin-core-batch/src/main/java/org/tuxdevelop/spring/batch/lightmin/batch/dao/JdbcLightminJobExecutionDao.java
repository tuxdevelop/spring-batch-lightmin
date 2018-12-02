package org.tuxdevelop.spring.batch.lightmin.batch.dao;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.repository.dao.JdbcJobExecutionDao;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.StringUtils;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminApplicationException;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;

@Slf4j
public class JdbcLightminJobExecutionDao extends JdbcJobExecutionDao implements LightminJobExecutionDao, InitializingBean {

    private static final String FIELDS = "E.JOB_EXECUTION_ID, E.START_TIME, E.END_TIME, E.STATUS, E.EXIT_CODE, E.EXIT_MESSAGE, "
            + "E.CREATE_TIME, E.LAST_UPDATED, E.VERSION, I.JOB_INSTANCE_ID, I.JOB_NAME";

    private final String GET_EXECUTION_COUNT =
            "SELECT COUNT(*) "
                    + "FROM %PREFIX%JOB_EXECUTION "
                    + "WHERE JOB_INSTANCE_ID = ?";

    private PagingQueryProvider byJobNamePagingQueryProvider;
    private PagingQueryProvider byJobInstanceIdExecutionsPagingQueryProvider;

    private final DataSource dataSource;

    public JdbcLightminJobExecutionDao(final DataSource dataSource) throws Exception {
        this.dataSource = dataSource;
    }

    @Override
    public List<JobExecution> findJobExecutions(final JobInstance jobInstance, final int start, final int count) {
        if (start <= 0) {
            return this.getJdbcTemplate().query(this.byJobInstanceIdExecutionsPagingQueryProvider.generateFirstPageQuery(count),
                    new JobExecutionRowMapper(jobInstance), jobInstance.getInstanceId());
        }
        try {
            final Long startAfterValue = this.getJdbcTemplate().queryForObject(
                    this.byJobInstanceIdExecutionsPagingQueryProvider.generateJumpToItemQuery(start, count), Long.class,
                    jobInstance.getInstanceId());
            return this.getJdbcTemplate().query(
                    this.byJobInstanceIdExecutionsPagingQueryProvider.generateRemainingPagesQuery(count),
                    new JobExecutionRowMapper(jobInstance), jobInstance.getInstanceId(), startAfterValue);
        } catch (final IncorrectResultSizeDataAccessException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public int getJobExecutionCount(final JobInstance jobInstance) {
        return this.getJdbcTemplate().queryForObject(this.getQuery(this.GET_EXECUTION_COUNT), Integer.class, jobInstance.getInstanceId());
    }

    @Override
    public List<JobExecution> getJobExecutions(final String jobName, final int start, final int count) {
        if (start <= 0) {
            return this.getJdbcTemplate().query(this.byJobNamePagingQueryProvider.generateFirstPageQuery(count),
                    new JobExecutionRowMapper(), jobName);
        }
        try {
            final Long startAfterValue = this.getJdbcTemplate().queryForObject(
                    this.byJobNamePagingQueryProvider.generateJumpToItemQuery(start, count), Long.class, jobName);
            return this.getJdbcTemplate().query(this.byJobNamePagingQueryProvider.generateRemainingPagesQuery(count),
                    new JobExecutionRowMapper(), jobName, startAfterValue);
        } catch (final IncorrectResultSizeDataAccessException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public List<JobExecution> findJobExecutions(final String jobName, final Map<String, Object> queryParameter, final Integer size) {
        final Boolean withJobName = StringUtils.hasText(jobName) ? Boolean.TRUE : Boolean.FALSE;
        final QueryParameterWrapper queryParameterWrapper = this.prepapreQueryParameterWrapper(queryParameter, withJobName, jobName);
        try {
            final PagingQueryProvider queryProvider = this.getPagingQueryProviderForQueryService(null, queryParameterWrapper.getQuery(), withJobName);
            return this.getJdbcTemplate().query(
                    queryProvider.generateFirstPageQuery(size),
                    queryParameterWrapper.getValues(),
                    queryParameterWrapper.getTypes(),
                    new JobExecutionRowMapper()
            );
        } catch (final Exception e) {
            throw new SpringBatchLightminApplicationException(e, "Could not execute Query, cause: " + e.getCause());
        }
    }

    /**
     * Ported from
     * {@link JdbcJobExecutionDao}
     */
    private final class JobExecutionRowMapper implements RowMapper<JobExecution> {

        private final JobInstance jobInstance;
        private JobParameters jobParameters;

        JobExecutionRowMapper() {
            this(null);
        }

        JobExecutionRowMapper(final JobInstance jobInstance) {
            this.jobInstance = jobInstance;
        }

        @Override
        public JobExecution mapRow(final ResultSet resultSet, final int rowNumber) throws SQLException {
            final Long id = resultSet.getLong(1);
            final String jobConfigurationLocation = resultSet.getString(10);
            if (this.jobParameters == null) {
                this.jobParameters = JdbcLightminJobExecutionDao.this.getJobParameters(id);
            }

            final JobExecution jobExecution;
            if (this.jobInstance == null) {
                jobExecution = new JobExecution(id, this.jobParameters, jobConfigurationLocation);
            } else {
                jobExecution = new JobExecution(this.jobInstance, id, this.jobParameters, jobConfigurationLocation);
            }

            jobExecution.setStartTime(resultSet.getTimestamp(2));
            jobExecution.setEndTime(resultSet.getTimestamp(3));
            jobExecution.setStatus(BatchStatus.valueOf(resultSet.getString(4)));
            jobExecution.setExitStatus(new ExitStatus(resultSet.getString(5), resultSet.getString(6)));
            jobExecution.setCreateTime(resultSet.getTimestamp(7));
            jobExecution.setLastUpdated(resultSet.getTimestamp(8));
            jobExecution.setVersion(resultSet.getInt(9));
            return jobExecution;
        }
    }

    /**
     * Ported From Spring Batch Admin Searchable JdbcSearchableJobExecutionDao
     */

    /**
     * @return a {@link PagingQueryProvider} for all job executions with the
     * provided where clause
     * @throws Exception
     */
    private PagingQueryProvider getPagingQueryProvider(final String whereClause) throws Exception {
        return this.getPagingQueryProvider(null, whereClause);
    }

    /**
     * @return a {@link PagingQueryProvider} with a where clause to narrow the
     * query
     * @throws Exception
     */
    private PagingQueryProvider getPagingQueryProvider(String fromClause, String whereClause) throws Exception {
        final SqlPagingQueryProviderFactoryBean factory = new SqlPagingQueryProviderFactoryBean();
        factory.setDataSource(this.dataSource);
        fromClause = "%PREFIX%JOB_EXECUTION E, %PREFIX%JOB_INSTANCE I" + (fromClause == null ? "" : ", " + fromClause);
        factory.setFromClause(this.getQuery(fromClause));
        factory.setSelectClause(FIELDS);
        final Map<String, Order> sortKeys = new HashMap<>();
        sortKeys.put("JOB_EXECUTION_ID", Order.DESCENDING);
        factory.setSortKeys(sortKeys);
        whereClause = "E.JOB_INSTANCE_ID=I.JOB_INSTANCE_ID" + (whereClause == null ? "" : " and " + whereClause);
        factory.setWhereClause(whereClause);

        return factory.getObject();
    }

    private PagingQueryProvider getPagingQueryProviderForQueryService(String fromClause, String whereClause, final Boolean withJobName) throws Exception {
        final SqlPagingQueryProviderFactoryBean factory = new SqlPagingQueryProviderFactoryBean();
        factory.setDataSource(this.dataSource);
        fromClause = "%PREFIX%JOB_EXECUTION E, %PREFIX%JOB_INSTANCE I" + (fromClause == null ? "" : ", " + fromClause);
        factory.setFromClause(this.getQuery(fromClause));
        factory.setSelectClause(FIELDS);
        final Map<String, Order> sortKeys = new HashMap<>();
        sortKeys.put("JOB_EXECUTION_ID", Order.DESCENDING);
        factory.setSortKeys(sortKeys);
        whereClause = "%s E.JOB_INSTANCE_ID=I.JOB_INSTANCE_ID" + (whereClause == null ? "" : whereClause);
        if (withJobName) {
            whereClause = String.format(whereClause, " I.JOB_NAME=? AND ");
        } else {
            whereClause = String.format(whereClause, "");
        }
        factory.setWhereClause(whereClause);
        return factory.getObject();
    }

    private QueryParameterWrapper prepapreQueryParameterWrapper(final Map<String, Object> queryParameters, final Boolean withJobName, final String jobNName) {
        final StringBuilder stringBuilder = new StringBuilder();
        final QueryParameterWrapper queryParameterWrapper = new QueryParameterWrapper();
        final List<Object> parameterValues = new ArrayList<>();
        final List<Integer> parameterTypes = new ArrayList<>();
        if (withJobName) {
            parameterValues.add(jobNName);
            parameterTypes.add(Types.VARCHAR);
        } else {
            log.debug("Querying for all job names");
        }
        if (queryParameters.containsKey(QueryParameterKey.EXIT_STATUS)) {
            stringBuilder.append(" AND E.STATUS = ?");
            parameterValues.add(queryParameters.get(QueryParameterKey.EXIT_STATUS));
            parameterTypes.add(Types.VARCHAR);
        }
        if (queryParameters.containsKey(QueryParameterKey.START_DATE)) {
            stringBuilder.append(" AND E.START_TIME > ?");
            parameterValues.add(DaoUtil.castDate(queryParameters.get(QueryParameterKey.START_DATE)));
            parameterTypes.add(Types.TIMESTAMP);
        }
        if (queryParameters.containsKey(QueryParameterKey.END_DATE)) {
            stringBuilder.append(" AND E.END_TIME < ?");
            parameterValues.add(DaoUtil.castDate(queryParameters.get(QueryParameterKey.END_DATE)));
            parameterTypes.add(Types.TIMESTAMP);
        }
        final Object[] values = parameterValues.toArray();
        final Integer[] types = parameterTypes.toArray(new Integer[parameterTypes.size()]);
        final int[] typesPrimitiv = new int[types.length];
        for (int i = 0; i < types.length; i++) {
            typesPrimitiv[i] = types[i];
        }
        queryParameterWrapper.setQuery(stringBuilder.toString());
        queryParameterWrapper.setValues(values);
        queryParameterWrapper.setTypes(typesPrimitiv);
        return queryParameterWrapper;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        this.byJobNamePagingQueryProvider = this.getPagingQueryProvider("I.JOB_NAME=?");
        this.byJobInstanceIdExecutionsPagingQueryProvider = this.getPagingQueryProvider("I.JOB_INSTANCE_ID=?");
    }

    @Data
    private final class QueryParameterWrapper {
        private String query;
        private Object[] values;
        private int[] types;
    }
}