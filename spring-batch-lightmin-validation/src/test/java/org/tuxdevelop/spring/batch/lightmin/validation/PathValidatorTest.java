package org.tuxdevelop.spring.batch.lightmin.validation;

import org.assertj.core.api.BDDAssertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.tuxdevelop.spring.batch.lightmin.validation.annotation.PathExists;
import org.tuxdevelop.spring.batch.lightmin.validation.validator.PathValidator;

import javax.validation.ConstraintValidatorContext;

public class PathValidatorTest {

    private PathValidator validator;

    @Before
    public void before() {
        validator = new PathValidator();
    }

    @Before
    public void after() {
        validator = null;
    }

    @Test
    public void testPathValidatorNullCase() {
        final String PATH = null;
        PathExists mock = Mockito.mock(PathExists.class);
        Mockito.when(mock.ignoreNull()).thenReturn(false);
        validator.initialize(mock);

        boolean valid = validator.isValid(null, null);
        BDDAssertions.assertThat(valid).isFalse();
    }

    @Test
    public void testPathValidatorNullIgnoreCase() {
        final String PATH = null;
        PathExists mock = Mockito.mock(PathExists.class);
        Mockito.when(mock.ignoreNull()).thenReturn(true);
        validator.initialize(mock);

        boolean valid = validator.isValid(null, null);
        BDDAssertions.assertThat(valid).isTrue();
    }

    @Test
    public void testPathValidatorInvalidPathCombination() {
        final String PATH_1 = "someTestPath///falsy";
        final String PATH_2 = "./someTestPath/falsy";
        PathExists mock = Mockito.mock(PathExists.class);
        Mockito.when(mock.ignoreNull()).thenReturn(false);
        validator.initialize(mock);

        BDDAssertions.assertThat(validator.isValid(PATH_1, null)).isFalse();
        BDDAssertions.assertThat(validator.isValid(PATH_2, null)).isFalse();
    }


    @Test
    public void testPathValidatorValidPathCombination() {
        final String PATH_1 = "src/workingdir";
        final String PATH_2 = "/src/workingdir";

        PathExists mock = Mockito.mock(PathExists.class);
        validator.initialize(mock);
        Mockito.when(mock.ignoreNull()).thenReturn(false);
        BDDAssertions.assertThat(validator.isValid(PATH_1, null)).isTrue();
        BDDAssertions.assertThat(validator.isValid(PATH_2, null)).isTrue();

    }
}
