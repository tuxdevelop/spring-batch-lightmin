package org.tuxdevelop.spring.batch.lightmin.address_migrator.domain;


import lombok.Data;

import java.io.Serializable;

@Data
public class BatchTaskAddress implements Serializable{

    public static final long serialVersionUID = 1L;

    private Long batchTaskId;
    private Long processingState;
    private String street;
    private String houseNumber;
    private String zipCode;
    private String city;
}
