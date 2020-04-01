package org.tuxdevelop.spring.batch.lightmin.test;

import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.rule.impl.*;
import com.openpojo.validation.test.impl.GetterTester;
import com.openpojo.validation.test.impl.SetterTester;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Before;
import org.junit.Test;

public abstract class PojoTestBase {

    private Validator pojoValidator;

    protected void testStructureAndBehavior(final Class<?> clazz) {
        final PojoClass pojoClass = PojoClassFactory.getPojoClass(clazz);
        this.pojoValidator.validate(pojoClass);
    }

    protected void testEquals(final Class<?> clazz) {
        EqualsVerifier
                .forClass(clazz)
                .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS, Warning.REFERENCE_EQUALITY)
                .verify();
    }

    @Test
    public abstract void performPojoTest();

    @Before
    public void init() {
      pojoValidator =  ValidatorBuilder.create()
                .with(new NoPublicFieldsRule(),new NoStaticExceptFinalRule(), new NoFieldShadowingRule(), new GetterMustExistRule(),new SetterMustExistRule())
                .with(new SetterTester(),new GetterTester()).build();
    }
}
