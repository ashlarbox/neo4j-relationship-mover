package org.ashlarbox.neo4j.relationship.move;

import org.neo4j.graphdb.Relationship;

import java.util.HashMap;

import static org.ashlarbox.neo4j.constants.OptionConstants.EXCLUDE_NEW_PROPERTY;
import static org.ashlarbox.neo4j.option.ValidateAndRetrieveOption.validateAndRetrieve;

class RelationshipPropertyCopier {

    void copy(Relationship oldRelationship, Relationship newRelationship, HashMap<String, Object> options) {
        String excludePropertyKey = (String) validateAndRetrieve(options, EXCLUDE_NEW_PROPERTY, String.class);
        for (String key : oldRelationship.getPropertyKeys()) {
            if (!key.equals(excludePropertyKey)) {
                newRelationship.setProperty(key, oldRelationship.getProperty(key));
            }
        }
    }
}
