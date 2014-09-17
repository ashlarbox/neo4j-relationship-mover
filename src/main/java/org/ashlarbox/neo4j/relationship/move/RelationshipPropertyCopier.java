package org.ashlarbox.neo4j.relationship.move;

import org.neo4j.graphdb.Relationship;

public class RelationshipPropertyCopier {

    protected void copy(Relationship oldRelationship, Relationship newRelationship, String excludeProperty) {
        for (String key : oldRelationship.getPropertyKeys()) {
            if (!key.equals(excludeProperty)) {
                newRelationship.setProperty(key, oldRelationship.getProperty(key));
            }
        }
    }
}
