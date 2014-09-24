package org.ashlarbox.neo4j.relationship.move;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import java.util.HashMap;

import static java.lang.String.format;

public class RelationshipMover {

    private RelationshipPropertyCopier relationshipPropertyCopier = new RelationshipPropertyCopier();
    private RelationshipPropertyAdder relationshipPropertyAdder = new RelationshipPropertyAdder();

    public void move(Node fromNode, Node toNode, Relationship oldRelationship, HashMap<String, Object> options) {

        Node newFromNode = oldRelationship.getStartNode().equals(fromNode)
                         ? toNode
                         : oldRelationship.getStartNode();

        Node newToNode = newFromNode.equals(toNode)
                       ? oldRelationship.getEndNode()
                       : toNode;

        Relationship newRelationship = newFromNode.createRelationshipTo(newToNode, oldRelationship.getType());

        relationshipPropertyCopier.copy(oldRelationship, newRelationship, options);
        relationshipPropertyAdder.add(newRelationship, options);

        System.out.println(format("Created new relationship from %s to %s. Type is %s",
                newFromNode, newToNode, newRelationship.getType().name()));
        System.out.println(format("Deleted old relationship from %s to %s. Type was %s",
                oldRelationship.getStartNode(), oldRelationship.getEndNode(), oldRelationship.getType().name()));

        oldRelationship.delete();
    }

}
