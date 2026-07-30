package org.tuxdevelop.spring.batch.lightmin.server.fe.model.validator;

import org.tuxdevelop.spring.batch.lightmin.api.resource.util.ApiParameterParser;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class JobParametersValidator implements ConstraintValidator<ValidJobParameters, String> {

    private ValidJobParameters validJobParameters;

    @Override
    public void initialize(final ValidJobParameters constraintAnnotation) {
        this.validJobParameters = constraintAnnotation;
    }

    @Override
    public boolean isValid(final String value, final ConstraintValidatorContext context) {
        Boolean isValid;
        if (value != null) {
            try {
                ApiParameterParser.parseParametersToJobParameters(value);
                isValid = Boolean.TRUE;
            } catch (final Exception e) {
                isValid = Boolean.FALSE;
            }
        } else {
            if (this.validJobParameters.ignoreNull()) {
                isValid = Boolean.TRUE;
            } else {
                isValid = Boolean.FALSE;
            }
        }
        return isValid;
    }
}
