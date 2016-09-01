package org.tuxdevelop.spring.batch.lightmin.client;

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
        pojoValidator.runValidation(pojoClass);
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
        pojoValidator = new PojoValidator();
        pojoValidator.addRule(new NoPublicFieldsRule());
        pojoValidator.addRule(new NoStaticExceptFinalRule());
        pojoValidator.addRule(new NoFieldShadowingRule());
        pojoValidator.addRule(new GetterMustExistRule());
        pojoValidator.addRule(new SetterMustExistRule());
        pojoValidator.addTester(new SetterTester());
        pojoValidator.addTester(new GetterTester());
    }
}
