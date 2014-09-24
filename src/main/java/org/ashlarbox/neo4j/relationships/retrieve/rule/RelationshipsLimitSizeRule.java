package org.ashlarbox.neo4j.relationships.retrieve.rule;

import com.google.common.collect.FluentIterable;
import org.neo4j.graphdb.Relationship;

import java.util.HashMap;

import static org.ashlarbox.neo4j.constants.OptionConstants.MOVE_LIMIT;
import static org.ashlarbox.neo4j.option.ValidateAndRetrieveOption.validateAndRetrieve;

public class RelationshipsLimitSizeRule {

    public FluentIterable<Relationship> apply(FluentIterable<Relationship> iterable, HashMap<String, Object> options) {
        Integer moveLimit = (Integer) validateAndRetrieve(options, MOVE_LIMIT, Integer.class);
        return (moveLimit == null) ? iterable : iterable.limit(moveLimit);
    }

}
