package org.ashlarbox.neo4j.relationships.retrieve;

import com.google.common.collect.FluentIterable;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import java.util.HashMap;
import java.util.List;

import static com.google.common.collect.FluentIterable.from;
import static org.ashlarbox.neo4j.constants.OptionConstants.DIRECTION;
import static org.ashlarbox.neo4j.constants.OptionConstants.MOVE_LIMIT;
import static org.ashlarbox.neo4j.relationship.predicate.NotWithNodePredicate.notWithNode;
import static org.ashlarbox.neo4j.util.FluentIterableUtil.applyLimit;
import static org.neo4j.graphdb.Direction.BOTH;

public class RelationshipsRetriever {

    public static List<Relationship> retrieveRelationships(Node sourceNode, Node excludeNode, HashMap<String, Object> options) {

        Direction direction = options.containsKey(DIRECTION) ? (Direction) options.get(DIRECTION) : BOTH;

        Iterable<Relationship> relationships = sourceNode.getRelationships(direction);

        FluentIterable<Relationship> iterable
                = from(relationships).filter(notWithNode(excludeNode));

        iterable = applyLimit(iterable, (Integer) options.get(MOVE_LIMIT));

        return iterable.toList();
    }

}
