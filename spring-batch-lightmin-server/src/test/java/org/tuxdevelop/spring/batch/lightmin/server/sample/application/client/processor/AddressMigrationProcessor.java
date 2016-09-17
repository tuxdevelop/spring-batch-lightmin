package org.tuxdevelop.spring.batch.lightmin.server.sample.application.client.processor;


import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tuxdevelop.spring.batch.lightmin.server.sample.application.client.domain.Address;
import org.tuxdevelop.spring.batch.lightmin.server.sample.application.client.domain.BatchTaskAddress;
import org.tuxdevelop.spring.batch.lightmin.server.sample.application.client.domain.ProcessingState;
import org.tuxdevelop.spring.batch.lightmin.server.sample.application.client.persistence.dao.AddressDao;

@Slf4j
@Component
public class AddressMigrationProcessor implements ItemProcessor<BatchTaskAddress, BatchTaskAddress> {

    private final AddressDao addressDAO;

    @Autowired
    public AddressMigrationProcessor(final AddressDao addressDAO) {
        this.addressDAO = addressDAO;
    }

    @Override
    public BatchTaskAddress process(final BatchTaskAddress batchTaskAddress) throws Exception {
        final Address address = mapToAddress(batchTaskAddress);
        addressDAO.add(address);
        batchTaskAddress.setProcessingState(ProcessingState.SUCCESS);
        return batchTaskAddress;
    }

    private Address mapToAddress(final BatchTaskAddress batchTaskAddress) {
        final Address address = new Address();
        final String streetLine = batchTaskAddress.getStreet() + " " + batchTaskAddress.getHouseNumber();
        final String cityLine = batchTaskAddress.getZipCode() + " " + batchTaskAddress.getCity();
        address.setId(batchTaskAddress.getBatchTaskId());
        address.setStreetLine(streetLine);
        address.setCityLine(cityLine);
        log.info("creating Address with street line: " + streetLine + " and city line: " + cityLine);
        return address;
    }
}
