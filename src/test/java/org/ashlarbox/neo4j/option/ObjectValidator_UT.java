package org.ashlarbox.neo4j.option;

import org.junit.Test;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.ashlarbox.neo4j.option.ObjectValidator.validForClass;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ObjectValidator_UT {

    @Test
    public void nullObjectAlwaysReturnsTrue() {
        assertThat(validForClass(null, Object.class), is(true));
    }

    @Test
    public void objectOfClassTypeReturnsTrue() {
        assertThat(validForClass(randomAlphabetic(5), String.class), is(true));
        assertThat(validForClass(nextInt(0, 5), Integer.class), is(true));
    }

    @Test
    public void objectOfAnotherClassTypeReturnsFalse() {
        assertThat(validForClass(randomAlphabetic(5), Integer.class), is(false));
    }

    @Test
    public void objectOfSubclassShouldReturnTrue() {
        assertThat(validForClass(new TestClassSub(), TestClassSuper.class), is(true));
    }

    @Test
    public void objectOfSuperclassShouldReturnFalse() {
        assertThat(validForClass(new TestClassSuper(), TestClassSub.class), is(false));
    }


    private class TestClassSuper {}

    private class TestClassSub extends TestClassSuper {}
}
