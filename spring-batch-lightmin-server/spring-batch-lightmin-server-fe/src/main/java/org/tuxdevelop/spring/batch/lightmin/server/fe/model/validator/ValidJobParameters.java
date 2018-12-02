package org.tuxdevelop.spring.batch.lightmin.server.fe.model.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = JobParametersValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidJobParameters {

    String message() default "{org.tuxdevelop.spring.batch.lightmin.validation.javax.validator.ValidJobParameters.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    boolean ignoreNull() default true;

    String value() default "";
}
