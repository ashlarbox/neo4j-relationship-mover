package org.ashlarbox.neo4j.relationships.retrieve.rule;

import com.google.common.collect.FluentIterable;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import static org.ashlarbox.neo4j.relationship.predicate.NotWithNodePredicate.notWithNode;

public class RetrieveExcludeNodeRule {

    public FluentIterable<Relationship> apply(FluentIterable<Relationship> iterable, Node excludeNode) {
        return iterable.filter(notWithNode(excludeNode));
    }

}
