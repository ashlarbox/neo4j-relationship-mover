package org.ashlarbox.neo4j.option;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.HashMap;

import static com.google.common.collect.Maps.newHashMap;
import static java.lang.String.format;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.ashlarbox.neo4j.option.ValidateAndRetrieveOption.validateAndRetrieve;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.rules.ExpectedException.none;

public class ValidateAndRetrieveOption_UT {

    private HashMap<String, Object> options;
    private String key = randomAlphanumeric(8);
    private String value = randomAlphanumeric(20);
    private Integer invalid = nextInt(0,20);

    @Rule
    public final ExpectedException expectedException = none();

    @Before
    public void initializeOptions() {
        options = newHashMap();
    }

    @Test
    public void optionsWithoutKeyShouldReturnNull() {
        assertThat(validateAndRetrieve(options, key, String.class), is(nullValue()));
    }

    @Test
    public void optionsWithKeyValueForClassShouldReturnValue() {
        options.put(key, value);
        assertThat((String) validateAndRetrieve(options, key, String.class), is(value));
    }

    @Test
    public void optionsWithKeyValueForAnotherClassShouldThrowException() {
        options.put(key, invalid);

        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(format("Option %s value is not of expected type %s", key, String.class.getName()));

        validateAndRetrieve(options, key, String.class);
    }

}
