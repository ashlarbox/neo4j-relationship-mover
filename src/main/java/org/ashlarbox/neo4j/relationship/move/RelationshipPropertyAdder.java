package org.ashlarbox.neo4j.relationship.move;

import org.neo4j.graphdb.Relationship;

import java.util.HashMap;

import static org.ashlarbox.neo4j.constants.OptionConstants.ADD_PROPERTY_KEY;
import static org.ashlarbox.neo4j.constants.OptionConstants.ADD_PROPERTY_VALUE;

public class RelationshipPropertyAdder {

    void add(Relationship newRelationship, HashMap<String, Object> options) {
        if (options.containsKey(ADD_PROPERTY_KEY) && options.containsKey(ADD_PROPERTY_VALUE)) {
            newRelationship.setProperty((String) options.get(ADD_PROPERTY_KEY), options.get(ADD_PROPERTY_VALUE));
        }
    }

}
