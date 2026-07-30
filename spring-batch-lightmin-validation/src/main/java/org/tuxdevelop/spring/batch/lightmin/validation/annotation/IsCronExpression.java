package org.tuxdevelop.spring.batch.lightmin.validation.annotation;

import org.tuxdevelop.spring.batch.lightmin.validation.validator.CronExpressionValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = CronExpressionValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface IsCronExpression {

    String message() default "{org.tuxdevelop.spring.batch.lightmin.validation.jakarta.validator.IsCronExpression.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    boolean ignoreNull() default true;

    String value() default "";
}
