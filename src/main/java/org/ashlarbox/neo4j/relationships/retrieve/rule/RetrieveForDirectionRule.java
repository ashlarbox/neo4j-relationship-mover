package org.ashlarbox.neo4j.relationships.retrieve.rule;

import com.google.common.collect.FluentIterable;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;

import java.util.HashMap;

import static com.google.common.collect.FluentIterable.from;
import static org.ashlarbox.neo4j.constants.OptionConstants.DIRECTION;
import static org.neo4j.graphdb.Direction.BOTH;

public class RetrieveForDirectionRule {

    public FluentIterable apply(Node sourceNode, HashMap<String, Object> options) {
        Direction direction = options.containsKey(DIRECTION) ? (Direction) options.get(DIRECTION) : BOTH;
        return from(sourceNode.getRelationships(direction));
    }

}
