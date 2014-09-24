package org.ashlarbox.neo4j.relationship.predicate;

import com.google.common.base.Predicate;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import java.util.ArrayList;

public class NotWithNodePredicate {

    public static Predicate<Relationship> notWithNode(final Node sourceNode, final ArrayList<Node> excludeNodes) {
        return new Predicate<Relationship>() {

            @Override
            public boolean apply(Relationship relationship) {
                Node otherNode = relationship.getOtherNode(sourceNode);
                return !excludeNodes.contains(otherNode);
            }
        };
    }
}
