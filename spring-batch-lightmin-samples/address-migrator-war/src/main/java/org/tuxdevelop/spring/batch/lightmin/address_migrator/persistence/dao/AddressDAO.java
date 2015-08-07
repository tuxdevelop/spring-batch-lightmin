package org.tuxdevelop.spring.batch.lightmin.address_migrator.persistence.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.tuxdevelop.spring.batch.lightmin.address_migrator.domain.Address;

import java.sql.Types;

@Component
public class AddressDAO {

    private static final String INSERT_STATEMENT = "INSERT INTO address (street_line, city_line)VALUES(?,?)";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void add(final Address address) {
        jdbcTemplate.update(INSERT_STATEMENT, new Object[]{address.getStreetLine(), address.getCityLine()}, new
                int[]{Types.VARCHAR, Types.VARCHAR});
    }
}
