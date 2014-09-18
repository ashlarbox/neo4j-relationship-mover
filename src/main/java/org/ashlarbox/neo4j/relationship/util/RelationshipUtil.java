package org.ashlarbox.neo4j.relationship.util;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import static java.util.Arrays.asList;

public class RelationshipUtil {

    public static boolean hasNode(Relationship relationship, Node node) {
        return asList(relationship.getNodes()).contains(node);
    }

}
