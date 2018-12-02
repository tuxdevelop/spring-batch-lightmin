package org.tuxdevelop.spring.batch.lightmin.server.fe.model.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CronExpressionValidator implements ConstraintValidator<IsCronExpression, String> {

    private IsCronExpression isCronExpression;

    @Override
    public void initialize(final IsCronExpression constraintAnnotation) {
        this.isCronExpression = constraintAnnotation;
    }

    @Override
    public boolean isValid(final String value, final ConstraintValidatorContext context) {
        final Boolean isValid;
        if (value != null) {
            isValid = org.quartz.CronExpression.isValidExpression(value);
        } else {
            if (this.isCronExpression.ignoreNull()) {
                isValid = Boolean.TRUE;
            } else {
                isValid = Boolean.FALSE;
            }
        }
        return isValid;
    }
}
