package org.tuxdevelop.spring.batch.lightmin.validation;

import org.assertj.core.api.BDDAssertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.tuxdevelop.spring.batch.lightmin.validation.annotation.PathExists;
import org.tuxdevelop.spring.batch.lightmin.validation.validator.PathValidator;

public class PathValidatorTest {

    private PathValidator validator;

    @Before
    public void before() {
        this.validator = new PathValidator();
    }

    @Before
    public void after() {
        this.validator = null;
    }

    @Test
    public void testPathValidatorNullCase() {
        final String PATH = null;
        final PathExists mock = Mockito.mock(PathExists.class);
        Mockito.when(mock.ignoreNull()).thenReturn(false);
        this.validator.initialize(mock);

        final boolean valid = this.validator.isValid(null, null);
        BDDAssertions.assertThat(valid).isFalse();
    }

    @Test
    public void testPathValidatorNullIgnoreCase() {
        final String PATH = null;
        final PathExists mock = Mockito.mock(PathExists.class);
        Mockito.when(mock.ignoreNull()).thenReturn(true);
        this.validator.initialize(mock);

        final boolean valid = this.validator.isValid(null, null);
        BDDAssertions.assertThat(valid).isTrue();
    }

    @Test
    public void testPathValidatorInvalidPathCombination() {
        final String PATH_1 = "someTestPath///falsy";
        final String PATH_2 = "./someTestPath/falsy";
        final PathExists mock = Mockito.mock(PathExists.class);
        Mockito.when(mock.ignoreNull()).thenReturn(false);
        this.validator.initialize(mock);

        BDDAssertions.assertThat(this.validator.isValid(PATH_1, null)).isFalse();
        BDDAssertions.assertThat(this.validator.isValid(PATH_2, null)).isFalse();
    }

    @Test
    public void testPathValidatorValidPathCombination() {
        final String PATH_1 = "src/workingdir";
        final String PATH_2 = "/src/workingdir";

        final PathExists mock = Mockito.mock(PathExists.class);
        this.validator.initialize(mock);
        Mockito.when(mock.ignoreNull()).thenReturn(false);
        BDDAssertions.assertThat(this.validator.isValid(PATH_1, null)).isTrue();
        BDDAssertions.assertThat(this.validator.isValid(PATH_2, null)).isTrue();

    }

    @Test
    public void testPathValidatorWindowsPath() {
        final String path = "D:\\Users\\test";
        BDDAssertions.assertThat(this.validator.isValid(path, null)).isTrue();
    }

}
