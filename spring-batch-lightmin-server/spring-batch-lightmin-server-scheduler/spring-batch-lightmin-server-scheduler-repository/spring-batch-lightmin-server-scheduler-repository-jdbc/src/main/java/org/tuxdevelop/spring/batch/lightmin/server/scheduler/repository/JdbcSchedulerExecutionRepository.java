package org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository;

import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.configuration.ServerSchedulerJdbcConfigurationProperties;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.SchedulerExecution;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.SchedulerValidationException;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.exception.SchedulerExecutionNotFoundException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.*;

public class JdbcSchedulerExecutionRepository implements SchedulerExecutionRepository {

    private static final String FIELDS = "S.id, S.scheduler_configuration_id, S.next_fire_time, S.execution_count, S.state, S.last_update, S.next_retry";

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
                    + SchedulerExecutionDomain.STATE + " = ?";

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

    private static final String DELETE_BY_SC_ID_AND_STATE =
            DELETE_BY_SC_ID
                    + " AND " + SchedulerExecutionDomain.STATE + " = ?";

    private static final String DELETE_BY_STATE =
            "DELETE FROM %s WHERE "
                    + SchedulerExecutionDomain.STATE + " = ?";

    private static final String UPDATE =
            "UPDATE %s SET "
                    + SchedulerExecutionDomain.SCHEDULER_CONFIGURATION_ID + " = ? , "
                    + SchedulerExecutionDomain.STATE + " = ? , "
                    + SchedulerExecutionDomain.NEXT_FIRE_TIME + " = ? , "
                    + SchedulerExecutionDomain.EXECUTION_COUNT + " = ? , "
                    + SchedulerExecutionDomain.LAST_UPDATE + " = ? , "
                    + SchedulerExecutionDomain.NEXT_RETRY + " = ? "
                    + " WHERE " + SchedulerExecutionDomain.ID + " = ?";

    private static final String GET_COUNT =
            "SELECT COUNT(1) FROM %s";

    private static final String GET_COUNT_BY_STATE =
            GET_COUNT + " WHERE "
                    + SchedulerExecutionDomain.STATE + " = ?";

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final String tableName;
    private final SchedulerExecutionRowMapper rowMapper;
    private final PagingQueryProvider byStatePagingQueryProvider;
    private final PagingQueryProvider findAllPagingQueryProvider;

    public JdbcSchedulerExecutionRepository(final JdbcTemplate jdbcTemplate,
                                            final ServerSchedulerJdbcConfigurationProperties properties) throws Exception {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withSchemaName(properties.getDatabaseSchema())
                .withTableName(properties.getExecutionTable())
                .usingGeneratedKeyColumns(SchedulerExecutionDomain.ID);
        this.tableName = properties.getExecutionTable();
        this.rowMapper = new SchedulerExecutionRowMapper();
        this.byStatePagingQueryProvider = this.getPagingQueryProvider("S.state = ?");
        this.findAllPagingQueryProvider = this.getPagingQueryProvider(null);
    }

    @Override
    public SchedulerExecution save(final SchedulerExecution schedulerExecution) {

        final SchedulerExecution result;
        if (schedulerExecution != null) {

            if (schedulerExecution.getId() == null) {
                result = this.create(schedulerExecution);
            } else {
                result = this.update(schedulerExecution);
            }
            return result;
        } else {
            throw new SchedulerValidationException("schedulerExecution must not be null");
        }
    }

    @Override
    public SchedulerExecution findById(final Long id) throws SchedulerExecutionNotFoundException {
        final String query = String.format(FIND_BY_ID, this.tableName);
        try {
            return this.jdbcTemplate.queryForObject(
                    query,
                    new Object[]{id},
                    new int[]{Types.NUMERIC},
                    this.rowMapper
            );
        } catch (final Exception e) {
            throw new SchedulerExecutionNotFoundException("Could not find SchedulerExecution for id " + id, e);
        }
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
    public List<SchedulerExecution> findAll(final int startIndex, final int pageSize) {
        if (startIndex <= 0) {
            return this.jdbcTemplate.query(this.findAllPagingQueryProvider.generateFirstPageQuery(pageSize),
                    this.rowMapper);
        }
        try {
            final Long startAfterValue = this.jdbcTemplate.queryForObject(
                    this.findAllPagingQueryProvider.generateJumpToItemQuery(startIndex, pageSize), Long.class);
            return this.jdbcTemplate.query(this.findAllPagingQueryProvider.generateRemainingPagesQuery(pageSize),
                    this.rowMapper, startAfterValue);
        } catch (final IncorrectResultSizeDataAccessException e) {
            return Collections.emptyList();
        }
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
    public List<SchedulerExecution> findByState(final Integer state, final int startIndex, final int pageSize) {
        if (startIndex <= 0) {
            return this.jdbcTemplate.query(this.byStatePagingQueryProvider.generateFirstPageQuery(pageSize),
                    this.rowMapper, state);
        }
        try {
            final Long startAfterValue = this.jdbcTemplate.queryForObject(
                    this.byStatePagingQueryProvider.generateJumpToItemQuery(startIndex, pageSize), Long.class, state);
            return this.jdbcTemplate.query(this.byStatePagingQueryProvider.generateRemainingPagesQuery(pageSize),
                    this.rowMapper, state, startAfterValue);
        } catch (final IncorrectResultSizeDataAccessException e) {
            return Collections.emptyList();
        }
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
    public Integer getExecutionCount(final Integer state) {
        final String sql;
        final Integer result;
        if (state != null) {
            sql = String.format(GET_COUNT_BY_STATE, this.tableName);
            result = this.jdbcTemplate.queryForObject(
                    sql,
                    new Object[]{state},
                    new int[]{Types.NUMERIC},
                    Integer.class
            );
        } else {
            sql = String.format(GET_COUNT, this.tableName);
            result = this.jdbcTemplate.queryForObject(
                    sql,
                    Integer.class
            );
        }
        return result;
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

    @Override
    public void deleteByConfigurationAndState(final Long configurationId, final Integer state) {
        final String sql = String.format(DELETE_BY_SC_ID_AND_STATE, this.tableName);
        this.jdbcTemplate.update(
                sql,
                new Object[]{
                        configurationId,
                        state},
                new int[]{
                        Types.NUMERIC,
                        Types.NUMERIC}
        );
    }

    private SchedulerExecution create(final SchedulerExecution schedulerExecution) {

        final Map<String, Object> keys = new HashMap<>();
        keys.put(SchedulerExecutionDomain.SCHEDULER_CONFIGURATION_ID, schedulerExecution.getSchedulerConfigurationId());
        keys.put(SchedulerExecutionDomain.NEXT_FIRE_TIME, schedulerExecution.getNextFireTime());
        keys.put(SchedulerExecutionDomain.EXECUTION_COUNT, schedulerExecution.getExecutionCount());
        keys.put(SchedulerExecutionDomain.STATE, schedulerExecution.getState());
        keys.put(SchedulerExecutionDomain.LAST_UPDATE, schedulerExecution.getLastUpdate());
        keys.put(SchedulerExecutionDomain.NEXT_RETRY, schedulerExecution.getNextRetry());

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
                        schedulerExecution.getLastUpdate(),
                        schedulerExecution.getNextRetry(),
                        schedulerExecution.getId()},
                new int[]{
                        Types.NUMERIC,
                        Types.NUMERIC,
                        Types.TIMESTAMP,
                        Types.NUMERIC,
                        Types.TIMESTAMP,
                        Types.TIMESTAMP,
                        Types.NUMERIC
                }
        );

        return schedulerExecution;
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
        sortKeys.put("id", Order.DESCENDING);
        factory.setSortKeys(sortKeys);
        whereClause = whereClause == null ? "" : whereClause;
        factory.setWhereClause(whereClause);
        return factory.getObject();
    }

    private static class SchedulerExecutionRowMapper implements RowMapper<SchedulerExecution> {

        @Override
        public SchedulerExecution mapRow(final ResultSet resultSet, final int i) throws SQLException {

            final SchedulerExecution schedulerExecution = new SchedulerExecution();
            schedulerExecution.setId(resultSet.getLong(SchedulerExecutionDomain.ID));
            schedulerExecution.setSchedulerConfigurationId(resultSet.getLong(SchedulerExecutionDomain.SCHEDULER_CONFIGURATION_ID));
            schedulerExecution.setNextFireTime(new Date(resultSet.getTimestamp(SchedulerExecutionDomain.NEXT_FIRE_TIME).getTime()));
            schedulerExecution.setState(resultSet.getInt(SchedulerExecutionDomain.STATE));
            schedulerExecution.setExecutionCount(resultSet.getInt(SchedulerExecutionDomain.EXECUTION_COUNT));
            schedulerExecution.setLastUpdate(new Date(resultSet.getTimestamp(SchedulerExecutionDomain.LAST_UPDATE).getTime()));
            final Timestamp nextRetryTimestamp = resultSet.getTimestamp(SchedulerExecutionDomain.NEXT_RETRY);
            final Date nextRetryDate = nextRetryTimestamp != null ? new Date(nextRetryTimestamp.getTime()) : null;
            schedulerExecution.setNextRetry(nextRetryDate);
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
        static final String LAST_UPDATE = "last_update";
        static final String NEXT_RETRY = "next_retry";
    }
}
