package org.tuxdevelop.spring.batch.lightmin.dao;

import org.springframework.batch.core.*;
import org.springframework.batch.core.repository.dao.JdbcJobExecutionDao;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class JdbcLightminJobExecutionDao extends JdbcJobExecutionDao implements LightminJobExecutionDao {

    private final String FIND_JOB_EXECUTIONS = "SELECT " +
            "JOB_EXECUTION_ID, " +
            "START_TIME, " +
            "END_TIME, " +
            "STATUS, " +
            "EXIT_CODE, " +
            "EXIT_MESSAGE, " +
            "CREATE_TIME, " +
            "LAST_UPDATED, " +
            "VERSION, " +
            "JOB_CONFIGURATION_LOCATION " +
            "FROM" +
            " %PREFIX%JOB_EXECUTION" +
            " WHERE" +
            " JOB_INSTANCE_ID = ? " +
            "ORDER BY JOB_EXECUTION_ID DESC";

    private final String GET_EXECUTION_COUNT = "SELECT " +
            "COUNT(*) " +
            "FROM %PREFIX%JOB_EXECUTION" +
            " WHERE JOB_INSTANCE_ID = ?";

    @Override
    public List<JobExecution> findJobExecutions(final JobInstance jobInstance, final int start, final int count) {
        final ResultSetExtractor extractor = new ResultSetExtractor() {
            private List<JobExecution> list = new ArrayList<JobExecution>();

            public List<JobExecution> extractData(ResultSet rs) throws SQLException, DataAccessException {
                int rowNumber;
                for (rowNumber = 0; rowNumber < start && rs.next(); ++rowNumber) {
                }

                while (rowNumber < start + count && rs.next()) {
                    final JobExecutionRowMapper rowMapper = new JdbcLightminJobExecutionDao.JobExecutionRowMapper(jobInstance);
                    list.add(rowMapper.mapRow(rs, rowNumber));
                    ++rowNumber;
                }
                return list;
            }
        };
        final List result = (List) getJdbcTemplate().query(getQuery(FIND_JOB_EXECUTIONS),
                new Object[]{jobInstance.getId()},
                extractor);
        return result;
    }

    @Override
    public int getJobExecutionCount(final JobInstance jobInstance) {
        return this.getJdbcTemplate().queryForObject(getQuery(GET_EXECUTION_COUNT), Integer.class,
                new Object[]{jobInstance.getInstanceId()});
    }

    /**
     * Ported from {@link org.springframework.batch.core.repository.dao.JdbcJobExecutionDao}
     */
    private final class JobExecutionRowMapper implements RowMapper<JobExecution> {

        private JobInstance jobInstance;
        private JobParameters jobParameters;

        public JobExecutionRowMapper(final JobInstance jobInstance) {
            this.jobInstance = jobInstance;
        }

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
}