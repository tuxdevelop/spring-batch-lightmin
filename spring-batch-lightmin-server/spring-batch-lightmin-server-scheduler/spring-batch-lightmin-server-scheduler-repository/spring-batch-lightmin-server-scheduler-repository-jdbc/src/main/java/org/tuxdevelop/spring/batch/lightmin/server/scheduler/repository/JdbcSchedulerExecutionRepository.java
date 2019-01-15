package org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.configuration.ServerSchedulerJdbcConfigurationProperties;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.SchedulerExecution;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JdbcSchedulerExecutionRepository implements SchedulerExecutionRepository {

    private static final String FIND_ALL =
            "SELECT * FROM %s";

    private static final String FIND_BY_ID =
            "SELECT * FROM %s WHERE "
                    + SchedulerExecutionDomain.ID + " = ?";

    private static final String FIND_BY_SC_ID =
            "SELECT * FROM %s WHERE "
                    + SchedulerExecutionDomain.SCHEDULER_CONFIGURATION_ID + " = ?";

    private static final String FIND_BY_LESS_EQUAL_DATE =
            "SELECT * FROM %s WHERE "
                    + SchedulerExecutionDomain.NEXT_FIRE_TIME + " <= ?";

    private static final String FIND_BY_STATE =
            "SELECT * FROM %s WHERE "
                    + SchedulerExecutionDomain.STATE + " <= ?";

    private static final String FIND_BY_STATE_AND_LESS_EQUAL_DATE =
            "SELECT * FROM %s WHERE "
                    + SchedulerExecutionDomain.STATE + " = ? "
                    + " AND "
                    + SchedulerExecutionDomain.NEXT_FIRE_TIME + " <= ?";

    private static final String DELETE_BY_ID =
            "DELETE FROM %s WHERE "
                    + SchedulerExecutionDomain.ID + " = ?";

    private static final String DELETE_BY_SC_ID =
            "DELETE FROM %s WHERE "
                    + SchedulerExecutionDomain.SCHEDULER_CONFIGURATION_ID + " = ?";

    private static final String DELETE_BY_STATE =
            "DELETE FROM %s WHERE "
                    + SchedulerExecutionDomain.STATE + " = ?";

    private static final String UPDATE =
            "UPDATE %s SET "
                    + SchedulerExecutionDomain.SCHEDULER_CONFIGURATION_ID + " = ? , "
                    + SchedulerExecutionDomain.STATE + " = ? , "
                    + SchedulerExecutionDomain.NEXT_FIRE_TIME + " = ?, "
                    + SchedulerExecutionDomain.EXECUTION_COUNT + " = ? "
                    + " WHERE " + SchedulerExecutionDomain.ID + " = ?";


    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final String tableName;
    private final SchedulerExecutionRowMapper rowMapper;

    public JdbcSchedulerExecutionRepository(final JdbcTemplate jdbcTemplate,
                                            final ServerSchedulerJdbcConfigurationProperties properties) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withSchemaName(properties.getDatabaseSchema())
                .withTableName(properties.getExecutionTable())
                .usingGeneratedKeyColumns(SchedulerExecutionDomain.ID);
        this.tableName = properties.getExecutionTable();
        this.rowMapper = new SchedulerExecutionRowMapper();
    }

    @Override
    public SchedulerExecution save(final SchedulerExecution schedulerExecution) {

        final SchedulerExecution result;

        if (schedulerExecution.getId() == null) {
            result = create(schedulerExecution);
        } else {
            result = update(schedulerExecution);
        }

        return result;
    }

    @Override
    public SchedulerExecution findById(final Long id) {
        final String query = String.format(FIND_BY_ID, this.tableName);
        return this.jdbcTemplate.queryForObject(
                query,
                new Object[]{id},
                new int[]{Types.NUMERIC},
                this.rowMapper
        );
    }

    @Override
    public void delete(final Long id) {
        final String sql = String.format(DELETE_BY_ID, this.tableName);
        this.jdbcTemplate.update(
                sql,
                new Object[]{id},
                new int[]{Types.NUMERIC}
        );
    }

    @Override
    public void deleteBySchedulerConfigurationId(final Long schedulerConfigurationId) {
        final String sql = String.format(DELETE_BY_SC_ID, this.tableName);
        this.jdbcTemplate.update(
                sql,
                new Object[]{schedulerConfigurationId},
                new int[]{Types.NUMERIC}
        );
    }

    @Override
    public List<SchedulerExecution> findAll() {
        final String query = String.format(FIND_ALL, this.tableName);
        return this.jdbcTemplate.query(
                query,
                this.rowMapper
        );
    }

    @Override
    public List<SchedulerExecution> findNextExecutions(final Date date) {
        final String query = String.format(FIND_BY_LESS_EQUAL_DATE, this.tableName);
        return this.jdbcTemplate.query(
                query,
                new Object[]{date},
                new int[]{Types.TIMESTAMP},
                this.rowMapper
        );
    }

    @Override
    public List<SchedulerExecution> findByState(final Integer state) {
        final String query = String.format(FIND_BY_STATE, this.tableName);
        return this.jdbcTemplate.query(
                query,
                new Object[]{state},
                new int[]{Types.NUMERIC},
                this.rowMapper
        );
    }

    @Override
    public List<SchedulerExecution> findByStateAndDate(final Integer state, final Date date) {
        final String query = String.format(FIND_BY_STATE_AND_LESS_EQUAL_DATE, this.tableName);
        return this.jdbcTemplate.query(
                query,
                new Object[]{state, date},
                new int[]{Types.NUMERIC, Types.TIMESTAMP},
                this.rowMapper
        );
    }

    @Override
    public List<SchedulerExecution> findBySchedulerConfigurationId(final Long schedulerConfigurationId) {
        final String query = String.format(FIND_BY_SC_ID, this.tableName);
        return this.jdbcTemplate.query(
                query,
                new Object[]{schedulerConfigurationId},
                new int[]{Types.NUMERIC},
                this.rowMapper
        );
    }

    @Override
    public void deleteByState(final Integer state) {
        final String sql = String.format(DELETE_BY_STATE, this.tableName);
        this.jdbcTemplate.update(
                sql,
                new Object[]{state},
                new int[]{Types.NUMERIC}
        );
    }

    private SchedulerExecution create(final SchedulerExecution schedulerExecution) {

        final Map<String, Object> keys = new HashMap<>();
        keys.put(SchedulerExecutionDomain.SCHEDULER_CONFIGURATION_ID, schedulerExecution.getSchedulerConfigurationId());
        keys.put(SchedulerExecutionDomain.NEXT_FIRE_TIME, schedulerExecution.getNextFireTime());
        keys.put(SchedulerExecutionDomain.EXECUTION_COUNT, schedulerExecution.getExecutionCount());
        keys.put(SchedulerExecutionDomain.STATE, schedulerExecution.getState());

        final Number id = this.simpleJdbcInsert.executeAndReturnKey(keys);
        schedulerExecution.setId(id.longValue());
        return schedulerExecution;
    }

    private SchedulerExecution update(final SchedulerExecution schedulerExecution) {
        final String sql = String.format(UPDATE, this.tableName);
        this.jdbcTemplate.update(
                sql,
                new Object[]{
                        schedulerExecution.getSchedulerConfigurationId(),
                        schedulerExecution.getState(),
                        schedulerExecution.getNextFireTime(),
                        schedulerExecution.getExecutionCount(),
                        schedulerExecution.getId()},
                new int[]{
                        Types.NUMERIC,
                        Types.NUMERIC,
                        Types.TIMESTAMP,
                        Types.NUMERIC,
                        Types.NUMERIC
                }
        );

        return schedulerExecution;
    }

    private static class SchedulerExecutionRowMapper implements RowMapper<SchedulerExecution> {

        @Override
        public SchedulerExecution mapRow(final ResultSet resultSet, final int i) throws SQLException {

            final SchedulerExecution schedulerExecution = new SchedulerExecution();
            schedulerExecution.setId(resultSet.getLong(SchedulerExecutionDomain.ID));
            schedulerExecution.setSchedulerConfigurationId(resultSet.getLong(SchedulerExecutionDomain.SCHEDULER_CONFIGURATION_ID));
            schedulerExecution.setNextFireTime(new Date(resultSet.getDate(SchedulerExecutionDomain.NEXT_FIRE_TIME).getTime()));
            schedulerExecution.setState(resultSet.getInt(SchedulerExecutionDomain.STATE));
            schedulerExecution.setExecutionCount(resultSet.getInt(SchedulerExecutionDomain.STATE));
            return schedulerExecution;
        }
    }

    // ########
    // Domain
    // ########

    private static final class SchedulerExecutionDomain {

        static final String ID = "id";
        static final String SCHEDULER_CONFIGURATION_ID = "scheduler_configuration_id";
        static final String NEXT_FIRE_TIME = "next_fire_time";
        static final String EXECUTION_COUNT = "execution_count";
        static final String STATE = "state";

    }
}
