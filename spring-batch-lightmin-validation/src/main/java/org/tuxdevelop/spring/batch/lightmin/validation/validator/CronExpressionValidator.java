package org.tuxdevelop.spring.batch.lightmin.validation.validator;

import org.springframework.util.StringUtils;
import org.tuxdevelop.spring.batch.lightmin.validation.annotation.IsCronExpression;

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
        Boolean isValid = Boolean.FALSE;
        if (this.isCronExpression.ignoreNull() && value == null) {
            return Boolean.TRUE;
        }
        if (value != null) {
            String[] fields = StringUtils.tokenizeToStringArray(value, " ");
            isValid = org.quartz.CronExpression.isValidExpression(value) && fields != null && fields.length == 6;
        }
        return isValid;
    }
}
