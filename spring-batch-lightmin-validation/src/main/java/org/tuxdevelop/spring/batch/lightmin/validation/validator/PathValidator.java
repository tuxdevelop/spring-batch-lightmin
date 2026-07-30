package org.tuxdevelop.spring.batch.lightmin.validation.validator;

import lombok.extern.slf4j.Slf4j;
import org.tuxdevelop.spring.batch.lightmin.validation.annotation.PathExists;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

@Slf4j
public class PathValidator implements ConstraintValidator<PathExists, String> {

    private PathExists constraint;

    private final static String PATH_PATTERN = "(([a-zA-Z]\\:)|(^\\/|[a-z]))([A-z0-9-_+]+\\/)*([A-z0-9]+)$";

    @Override
    public void initialize(final PathExists constraint) {
        this.constraint = constraint;
    }


    @Override
    public boolean isValid(final String obj, final ConstraintValidatorContext context) {

        if ((this.constraint == null || this.constraint.ignoreNull()) && obj == null) {
            return Boolean.TRUE;
        } else if (obj == null) {
            return Boolean.FALSE;
        }
        return Pattern.matches(PATH_PATTERN, obj);
    }
}

