package org.ashlarbox.neo4j.relationship.move;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.neo4j.graphdb.Relationship;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.ashlarbox.neo4j.constants.OptionConstants.EXCLUDE_NEW_PROPERTY;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RelationshipPropertyCopier_UT {

    private final RelationshipPropertyCopier relationshipPropertyCopier = new RelationshipPropertyCopier();

    @Mock private Relationship oldRelationship;
    @Mock private Relationship newRelationship;
    @Mock private HashMap<String,Object> options;

    @Test
    public void moverShouldCopyPropertiesToNewRelationship() {
        Map<String, String> propertyMap = newHashMap();
        int propertiesToAdd = nextInt(0, 5);
        for (int i = 0; i <= propertiesToAdd; i++) {
            String key = randomAlphabetic(8);
            String value = randomAlphanumeric(10);
            propertyMap.put(key, value);
            when(oldRelationship.getProperty(key)).thenReturn(value);
        }

        when(oldRelationship.getPropertyKeys()).thenReturn(propertyMap.keySet());
        when(options.get(EXCLUDE_NEW_PROPERTY)).thenReturn(null);

        relationshipPropertyCopier.copy(oldRelationship, newRelationship, options);

        for (Map.Entry<String, String> entry : propertyMap.entrySet()) {
            verify(newRelationship).setProperty(entry.getKey(), entry.getValue());
        }
    }

    @Test
    public void moverShouldOptionallyExcludePropertyFromNewRelationship() {
        Map<String, String> propertyMap = newHashMap();
        String excludeKey = randomAlphabetic(8);
        String value = randomAlphanumeric(10);
        propertyMap.put(excludeKey, value);

        when(oldRelationship.getPropertyKeys()).thenReturn(propertyMap.keySet());
        when(options.get(EXCLUDE_NEW_PROPERTY)).thenReturn(excludeKey);

        relationshipPropertyCopier.copy(oldRelationship, newRelationship, options);

        verifyZeroInteractions(newRelationship);
    }


}
