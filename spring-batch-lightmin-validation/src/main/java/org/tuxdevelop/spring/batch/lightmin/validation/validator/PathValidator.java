package org.tuxdevelop.spring.batch.lightmin.validation.validator;

import lombok.extern.slf4j.Slf4j;
import org.tuxdevelop.spring.batch.lightmin.validation.annotation.PathExists;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

@Slf4j
public class PathValidator implements ConstraintValidator<PathExists, String> {

    private PathExists constraint;

    private final String PATH_PATTERN = "(^\\/|[a-z])([A-z0-9-_+]+\\/)*([A-z0-9]+)$";

    public void initialize(PathExists constraint) {
        this.constraint = constraint;
    }


    public boolean isValid(String obj, ConstraintValidatorContext context) {

        if (constraint.ignoreNull() && obj == null) {
            return Boolean.TRUE;
        } else if (obj == null) {
            return Boolean.FALSE;
        }
        return Pattern.matches(PATH_PATTERN, obj);
    }
}

