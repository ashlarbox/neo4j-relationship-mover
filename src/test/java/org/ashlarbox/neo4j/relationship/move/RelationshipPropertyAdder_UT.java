package org.ashlarbox.neo4j.relationship.move;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.neo4j.graphdb.Relationship;

import java.util.HashMap;

import static com.google.common.collect.Maps.newHashMap;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.ashlarbox.neo4j.constants.OptionConstants.ADD_PROPERTY_KEY;
import static org.ashlarbox.neo4j.constants.OptionConstants.ADD_PROPERTY_VALUE;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(MockitoJUnitRunner.class)
public class RelationshipPropertyAdder_UT {

    private final RelationshipPropertyAdder relationshipPropertyAdder = new RelationshipPropertyAdder();

    @Mock private Relationship newRelationship;
    private HashMap<String, Object> options;
    private final String key = randomAlphanumeric(7);
    private final String value = randomAlphanumeric(4);


    @Before
    public void initializeOptions() {
        options = newHashMap();
    }

    @Test
    public void addShouldNotAddPropertyWhenNoOptionsToAdd() {
        relationshipPropertyAdder.add(newRelationship, options);
        verifyZeroInteractions(newRelationship);
    }

    @Test
    public void addShouldNotAddPropertyWhenNoPropertyToAddKey() {
        options.put(ADD_PROPERTY_VALUE, value);
        relationshipPropertyAdder.add(newRelationship, options);
        verifyZeroInteractions(newRelationship);
    }

    @Test
    public void addShouldNotAddPropertyWhenNoPropertyToAddValue() {
        options.put(ADD_PROPERTY_KEY, key);
        relationshipPropertyAdder.add(newRelationship, options);
        verifyZeroInteractions(newRelationship);
    }

    @Test
    public void addShouldAddPropertyWhenOptionsExist() {
        options.put(ADD_PROPERTY_KEY, key);
        options.put(ADD_PROPERTY_VALUE, value);
        relationshipPropertyAdder.add(newRelationship, options);
        verify(newRelationship).setProperty(key, value);
    }

}
