package org.ashlarbox.neo4j.relationships.retrieve.rule;

import com.google.common.collect.FluentIterable;

import java.util.HashMap;

import static org.ashlarbox.neo4j.constants.OptionConstants.HAS_PROPERTY;
import static org.ashlarbox.neo4j.relationship.predicate.HasPropertyPredicate.hasProperty;

public class RetrieveHasPropertyRule {

    public FluentIterable apply(FluentIterable iterable, HashMap<String, Object> options) {
        return options.containsKey(HAS_PROPERTY)
                ? iterable
                : iterable.filter(hasProperty((String) options.get(HAS_PROPERTY)));
    }

}
