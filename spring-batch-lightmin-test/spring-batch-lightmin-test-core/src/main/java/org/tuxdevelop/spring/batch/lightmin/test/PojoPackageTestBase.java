package org.tuxdevelop.spring.batch.lightmin.test;


import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.PojoClassFilter;
import com.openpojo.reflection.filters.FilterChain;
import com.openpojo.reflection.filters.FilterNestedClasses;
import com.openpojo.reflection.filters.FilterNonConcrete;
import com.openpojo.reflection.filters.FilterPackageInfo;
import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.affirm.Affirm;
import com.openpojo.validation.test.impl.GetterTester;
import com.openpojo.validation.test.impl.SetterTester;
import lombok.extern.slf4j.Slf4j;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

import java.util.List;

@Slf4j
public abstract class PojoPackageTestBase {

    private final FilterChain filterChain = new FilterChain(new FilterPackageInfo(), new FilterNonConcrete(), new FilterNestedClasses(), new FilterAbstractClasses());

    // The package to test
    private final String pojoPackage;
    // Configured for expectation, so we know when a class gets added or removed.
    private final Integer expectedClassCount;

    private final Boolean withEquals;

    private final Boolean withExpectedCount;

    public PojoPackageTestBase(final String pojoPackage) {
        this.pojoPackage = pojoPackage;
        this.expectedClassCount = -1;
        this.withEquals = Boolean.TRUE;
        this.withExpectedCount = Boolean.FALSE;
    }

    public PojoPackageTestBase(final String pojoPackage, final Integer expectedClassCount) {
        this.pojoPackage = pojoPackage;
        this.expectedClassCount = expectedClassCount;
        this.withEquals = Boolean.TRUE;
        this.withExpectedCount = Boolean.TRUE;
    }

    public PojoPackageTestBase(final String pojoPackage, final Boolean withEquals) {
        this.pojoPackage = pojoPackage;
        this.expectedClassCount = -1;
        this.withEquals = withEquals;
        this.withExpectedCount = Boolean.FALSE;
    }

    public PojoPackageTestBase(final String pojoPackage, final Integer expectedClassCount, final Boolean withEquals) {
        this.pojoPackage = pojoPackage;
        this.expectedClassCount = expectedClassCount;
        this.withEquals = withEquals;
        this.withExpectedCount = Boolean.TRUE;
    }

    @Test
    public void ensureExpectedPojoCount() {
        if (this.withExpectedCount) {
            final List<PojoClass> pojoClasses = PojoClassFactory.getPojoClasses(this.pojoPackage,
                    new FilterPackageInfo());
            Affirm.affirmEquals("Classes added / removed?", this.expectedClassCount, pojoClasses.size());
        } else {
            log.debug("WithExpectedCount is disbabled");
        }
    }

    @Test
    public void testEquals() {
        if (this.withEquals) {
            final List<PojoClass> pojoClasses = PojoClassFactory.getPojoClasses(this.pojoPackage, this.filterChain);
            for (final PojoClass pojoClass : pojoClasses) {
                final Class<?> clazz = pojoClass.getClazz();

                EqualsVerifier
                        .forClass(clazz)
                        .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
                        .verify();
            }
        } else {
            log.info("Equals Test is disbabled");
        }

    }

    @Test
    public void testPojoStructureAndBehavior() {
        final Validator validator = ValidatorBuilder.create()
                // Add Testers to validate behaviour for pojoPackage
                // See com.openpojo.validation.test.impl for more ...
                .with(new SetterTester())
                .with(new GetterTester())
                .build();

        validator.validate(this.pojoPackage, this.filterChain);
    }

    public class FilterAbstractClasses implements PojoClassFilter {

        @Override
        public boolean include(final PojoClass pojoClass) {
            return !pojoClass.isAbstract();
        }

    }
}