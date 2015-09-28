package org.tuxdevelop.spring.batch.lightmin.address_migrator.domain;


public class ProcessingState {

    public static final Long INIT = 10L;
    public static final Long SUCCESS = 40L;
    public static final Long PRINTED = 50L;
    public static final Long ERROR = 90L;
}
