package org.ashlarbox.neo4j.relationship.predicate;

import com.google.common.base.Predicate;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

public class WithLabelPredicate {

    private WithLabelPredicate() {}

    public static Predicate<Relationship> linkedWithLabel(final Node sourceNode, final Label label) {
        return new Predicate<Relationship>() {

            @Override
            public boolean apply(Relationship relationship) {
                return relationship.getOtherNode(sourceNode).hasLabel(label);
            }
        };
    }
}
