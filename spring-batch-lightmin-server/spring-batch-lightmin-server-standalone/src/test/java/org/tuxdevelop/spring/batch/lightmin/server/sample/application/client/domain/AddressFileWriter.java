package org.tuxdevelop.spring.batch.lightmin.server.sample.application.client.domain;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.fluttercode.datafactory.impl.DataFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.*;

@Slf4j
@Component
public class AddressFileWriter {

    private static final String PATH = "src/test/resources/properties/sample/client/input";
    private static final int FILE_ROWS = 100;

    private final DataFactory dataFactory;


    public AddressFileWriter() {
        this.dataFactory = new DataFactory();
    }


    @Scheduled(fixedDelay = 10000, initialDelay = 5000)
    public void writeFile() {
        final Long currentMillis = System.currentTimeMillis();
        final String fileName = "input_" + currentMillis + ".tmp";
        final File file = new File(PATH + File.separator + fileName);
        try {
            file.createNewFile();
        } catch (final IOException e) {
            e.printStackTrace();
        }
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(file), "utf-8"))) {
            writer.write("zip_code;city;street;house_number");
            for (int i = 0; i < FILE_ROWS; i++) {
                final BatchTaskAddress batchTaskAddress = createBatchTaskAddress();
                final String stringBuilder = batchTaskAddress.getZipCode() +
                        ";" +
                        batchTaskAddress.getCity() +
                        ";" +
                        batchTaskAddress.getStreet() +
                        ";" +
                        batchTaskAddress.getHouseNumber();
                writer.write("\n");
                writer.write(stringBuilder);
            }
            writer.flush();
            log.info("Created File: {} ", file.getAbsolutePath());
            FileUtils.moveFile(new File(PATH + File.separator + fileName), new File(PATH + File.separator + fileName + "" + ".txt"));
        } catch (final IOException e) {
            e.printStackTrace();
        }

    }


    private BatchTaskAddress createBatchTaskAddress() {
        final BatchTaskAddress batchTaskAddress = new BatchTaskAddress();
        final String street = this.dataFactory.getStreetName();
        final String houseNumber = this.dataFactory.getNumberText(4);
        final String zipCode = this.dataFactory.getNumberText(5);
        final String city = this.dataFactory.getCity();
        batchTaskAddress.setStreet(street);
        batchTaskAddress.setHouseNumber(houseNumber);
        batchTaskAddress.setZipCode(zipCode);
        batchTaskAddress.setCity(city);
        return batchTaskAddress;
    }

}
