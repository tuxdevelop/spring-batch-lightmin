package org.tuxdevelop.spring.batch.lightmin.server.sample.application.client.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class Address implements Serializable {

    public static final long serialVersionUID = 1L;

    private Long id;
    private String streetLine;
    private String cityLine;

}
