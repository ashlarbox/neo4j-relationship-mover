package org.ashlarbox.neo4j.relationship.predicate;

import com.google.common.base.Predicate;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import static org.ashlarbox.neo4j.relationship.util.RelationshipUtil.hasNode;

public class NotWithNodePredicate {

    public static Predicate<Relationship> notWithNode(final Node excludeNode) {
        return new Predicate<Relationship>() {

            @Override
            public boolean apply(Relationship relationship) {
                return !hasNode(relationship, excludeNode);
            }
        };
    }
}
