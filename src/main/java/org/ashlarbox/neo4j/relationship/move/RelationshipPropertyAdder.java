package org.ashlarbox.neo4j.relationship.move;

import org.neo4j.graphdb.Relationship;

import java.util.HashMap;

import static org.ashlarbox.neo4j.constants.OptionConstants.ADD_PROPERTY_KEY;
import static org.ashlarbox.neo4j.constants.OptionConstants.ADD_PROPERTY_VALUE;
import static org.ashlarbox.neo4j.option.ValidateAndRetrieveOption.validateAndRetrieve;

class RelationshipPropertyAdder {

    void add(Relationship newRelationship, HashMap<String, Object> options) {
        String addPropertyKey = (String) validateAndRetrieve(options, ADD_PROPERTY_KEY, String.class);
        if (addPropertyKey != null && options.containsKey(ADD_PROPERTY_VALUE)) {
            newRelationship.setProperty(addPropertyKey, options.get(ADD_PROPERTY_VALUE));
        }
    }

}
