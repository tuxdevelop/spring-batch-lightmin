package org.tuxdevelop.spring.batch.lightmin.server.cluster.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ServerClusterLock {

    long DEFAULT_TIMEOUT = 5;

    String id();

    boolean waitForLock() default false;

    long timeout() default DEFAULT_TIMEOUT;

    TimeUnit timeUnit() default TimeUnit.SECONDS;
}
