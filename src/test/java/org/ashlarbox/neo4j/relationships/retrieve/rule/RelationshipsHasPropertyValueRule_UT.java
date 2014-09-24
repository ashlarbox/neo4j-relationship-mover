package org.ashlarbox.neo4j.relationships.retrieve.rule;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.neo4j.graphdb.Relationship;

import java.util.HashMap;
import java.util.List;

import static com.google.common.collect.FluentIterable.from;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.ashlarbox.neo4j.constants.OptionConstants.HAS_PROPERTY_KEY;
import static org.ashlarbox.neo4j.constants.OptionConstants.HAS_PROPERTY_VALUE;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RelationshipsHasPropertyValueRule_UT {

    private final RelationshipsHasPropertyValueRule relationshipsHasPropertyValueRule = new RelationshipsHasPropertyValueRule();

    private final String key = randomAlphabetic(5);
    private final Integer value = nextInt(0, 5);
    private final String otherValue = randomAlphanumeric(10);

    private HashMap<String, Object> options;
    private List<Relationship> relationships;

    @Mock private Relationship goodRelationship;
    @Mock private Relationship relationshipMissingProperty;
    @Mock private Relationship relationshipWithOtherValue;

    @Before
    public void initializeOptions() {
        options = newHashMap();
    }

    @Before
    public void initializeRelationships() {
        relationships = newArrayList(goodRelationship, relationshipMissingProperty, relationshipWithOtherValue);
    }

    @Before
    public void mockGoodRelationship() {
        when(goodRelationship.hasProperty(key)).thenReturn(true);
        when(goodRelationship.getProperty(key)).thenReturn(value);
    }

    @Before
    public void mockRelationshipMissingProperty() {
        when(relationshipMissingProperty.hasProperty(key)).thenReturn(false);
    }

    @Before
    public void mockRelationshipWithOtherValue() {
        when(relationshipWithOtherValue.hasProperty(key)).thenReturn(true);
        when(relationshipWithOtherValue.getProperty(key)).thenReturn(otherValue);
    }

    @Test
    public void missingHasPropertyKeyOptionShouldNotApplyFilter() {
        options.put(HAS_PROPERTY_VALUE, value);
        List<Relationship> returnedRelationships = relationshipsHasPropertyValueRule.apply(from(relationships), options).toList();
        assertThat(returnedRelationships.size(), is(relationships.size()));
        assertThat(returnedRelationships.containsAll(relationships), is(true));
    }

    @Test
    public void missingHasPropertyValueOptionShouldNotApplyFilter() {
        options.put(HAS_PROPERTY_KEY, key);
        List<Relationship> returnedRelationships = relationshipsHasPropertyValueRule.apply(from(relationships), options).toList();
        assertThat(returnedRelationships.size(), is(relationships.size()));
        assertThat(returnedRelationships.containsAll(relationships), is(true));
    }

    @Test
    public void optionsHavingBothValuesShouldApplyFilter() {
        options.put(HAS_PROPERTY_KEY, key);
        options.put(HAS_PROPERTY_VALUE, value);
        List<Relationship> returnedRelationships = relationshipsHasPropertyValueRule.apply(from(relationships), options).toList();
        assertThat(returnedRelationships.size(), is(1));
        assertThat(returnedRelationships, hasItem(goodRelationship));
    }

}
