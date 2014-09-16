package org.ashlarbox.neo4j.relationship.move;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

public class RelationshipMover {

    public static void moveRelationship(Node fromNode, Node toNode, Relationship oldRelationship) {

        Node newFromNode = oldRelationship.getStartNode().equals(fromNode)
                         ? toNode
                         : oldRelationship.getStartNode();

        Node newToNode = newFromNode.equals(toNode)
                       ? oldRelationship.getEndNode()
                       : toNode;

        Relationship newRelationship = newFromNode.createRelationshipTo(newToNode, oldRelationship.getType());

        for (String key : oldRelationship.getPropertyKeys()) {
            newRelationship.setProperty(key, oldRelationship.getProperty(key));
        }


        oldRelationship.delete();
    }

}
