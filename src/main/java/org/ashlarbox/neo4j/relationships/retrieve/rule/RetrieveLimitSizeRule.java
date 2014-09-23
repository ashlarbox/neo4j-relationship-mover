package org.ashlarbox.neo4j.relationships.retrieve.rule;

import com.google.common.collect.FluentIterable;

import java.util.HashMap;

import static org.ashlarbox.neo4j.constants.OptionConstants.MOVE_LIMIT;

public class RetrieveLimitSizeRule {

    public FluentIterable apply(FluentIterable iterable, HashMap<String, Object> options) {
        Integer moveLimit = (Integer) options.get(MOVE_LIMIT);
        return (moveLimit == null) ? iterable : iterable.limit(moveLimit);
    }

}
