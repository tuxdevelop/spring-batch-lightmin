package org.tuxdevelop.spring.batch.lightmin.test;

import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.PojoValidator;
import com.openpojo.validation.rule.impl.*;
import com.openpojo.validation.test.impl.GetterTester;
import com.openpojo.validation.test.impl.SetterTester;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("deprecation")
public abstract class PojoTestBase {

    private PojoValidator pojoValidator;

    protected void testStructureAndBehavior(final Class<?> clazz) {
        final PojoClass pojoClass = PojoClassFactory.getPojoClass(clazz);
        this.pojoValidator.runValidation(pojoClass);
    }

    protected void testEquals(final Class<?> clazz) {
        EqualsVerifier
                .forClass(clazz)
                .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
                .verify();
    }

    @Test
    public abstract void performPojoTest();

    @Before
    public void init() {
        this.pojoValidator = new PojoValidator();
        this.pojoValidator.addRule(new NoPublicFieldsRule());
        this.pojoValidator.addRule(new NoStaticExceptFinalRule());
        this.pojoValidator.addRule(new NoFieldShadowingRule());
        this.pojoValidator.addRule(new GetterMustExistRule());
        this.pojoValidator.addRule(new SetterMustExistRule());
        this.pojoValidator.addTester(new SetterTester());
        this.pojoValidator.addTester(new GetterTester());
    }
}
