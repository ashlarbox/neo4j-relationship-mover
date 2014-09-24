package org.ashlarbox.neo4j.relationships.retrieve.rule;

import com.google.common.collect.FluentIterable;
import org.neo4j.graphdb.Relationship;

import java.util.HashMap;

import static org.ashlarbox.neo4j.constants.OptionConstants.HAS_PROPERTY_KEY;
import static org.ashlarbox.neo4j.constants.OptionConstants.HAS_PROPERTY_VALUE;
import static org.ashlarbox.neo4j.option.ValidateAndRetrieveOption.validateAndRetrieve;
import static org.ashlarbox.neo4j.relationship.predicate.HasPropertyPredicate.hasProperty;

public class RelationshipsHasPropertyValueRule {

    public FluentIterable<Relationship> apply(FluentIterable<Relationship> iterable, HashMap<String, Object> options) {

        String propertyKey = (String) validateAndRetrieve(options, HAS_PROPERTY_KEY, String.class);
        Object propertyValue = validateAndRetrieve(options, HAS_PROPERTY_VALUE, Object.class);

        return (propertyKey != null && propertyValue != null)
                ? iterable.filter(hasProperty(propertyKey, propertyValue))
                : iterable;
    }

}
