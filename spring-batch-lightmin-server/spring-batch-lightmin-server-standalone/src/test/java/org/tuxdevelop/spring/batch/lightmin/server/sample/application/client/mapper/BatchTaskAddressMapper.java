package org.tuxdevelop.spring.batch.lightmin.server.sample.application.client.mapper;


import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.tuxdevelop.spring.batch.lightmin.server.sample.application.client.domain.BatchTaskAddress;
import org.tuxdevelop.spring.batch.lightmin.server.sample.application.client.domain.BatchTaskAddressDomain;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class BatchTaskAddressMapper implements RowMapper<BatchTaskAddress> {
    @Override
    public BatchTaskAddress mapRow(final ResultSet resultSet, final int rowNum) throws SQLException {
        final BatchTaskAddress batchTaskAddress = new BatchTaskAddress();
        batchTaskAddress.setBatchTaskId(resultSet.getLong(BatchTaskAddressDomain.BATCH_TASK_ID));
        batchTaskAddress.setProcessingState(resultSet.getLong(BatchTaskAddressDomain.PROCESSING_STATE));
        batchTaskAddress.setStreet(resultSet.getString(BatchTaskAddressDomain.STREET));
        batchTaskAddress.setHouseNumber(resultSet.getString(BatchTaskAddressDomain.HOUSE_NUMBER));
        batchTaskAddress.setZipCode(resultSet.getString(BatchTaskAddressDomain.ZIP_CODE));
        batchTaskAddress.setCity(resultSet.getString(BatchTaskAddressDomain.CITY));
        return batchTaskAddress;
    }
}
