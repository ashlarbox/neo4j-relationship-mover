package org.ashlarbox.neo4j.relationship.move;

import org.neo4j.graphdb.Relationship;

import java.util.HashMap;

import static org.ashlarbox.neo4j.constants.OptionConstants.EXCLUDE_NEW_PROPERTY;

class RelationshipPropertyCopier {

    void copy(Relationship oldRelationship, Relationship newRelationship, HashMap<String, Object> options) {
        for (String key : oldRelationship.getPropertyKeys()) {
            if (!key.equals(options.get(EXCLUDE_NEW_PROPERTY))) {
                newRelationship.setProperty(key, oldRelationship.getProperty(key));
            }
        }
    }
}
