package org.ashlarbox.neo4j.relationships.retrieve.rule;

import com.google.common.collect.FluentIterable;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import java.util.HashMap;

import static com.google.common.collect.FluentIterable.from;
import static org.ashlarbox.neo4j.constants.OptionConstants.DIRECTION;
import static org.ashlarbox.neo4j.option.ValidateAndRetrieveOption.validateAndRetrieve;
import static org.neo4j.graphdb.Direction.BOTH;

public class RetrieveForDirectionRule {

    public FluentIterable<Relationship> apply(Node sourceNode, HashMap<String, Object> options) {
        Direction direction = (Direction) validateAndRetrieve(options, DIRECTION, Direction.class);
        return from(sourceNode.getRelationships(options.containsKey(DIRECTION) ? direction : BOTH));
    }

}
