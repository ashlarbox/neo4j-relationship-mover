package org.ashlarbox.neo4j.relationships.retrieve.rule;

import com.google.common.collect.FluentIterable;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import java.util.ArrayList;

import static org.ashlarbox.neo4j.relationship.predicate.NotWithNodePredicate.notWithNode;

public class RelationshipsExcludeWithNodesRule {

    public FluentIterable<Relationship> apply(FluentIterable<Relationship> iterable, Node sourceNode, ArrayList<Node> excludeNodes) {
        return iterable.filter(notWithNode(sourceNode, excludeNodes));
    }

}
