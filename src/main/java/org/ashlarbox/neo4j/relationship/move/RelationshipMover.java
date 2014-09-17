package org.ashlarbox.neo4j.relationship.move;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

public class RelationshipMover {

    private RelationshipPropertyCopier relationshipPropertyCopier = new RelationshipPropertyCopier();

    public void move(Node fromNode, Node toNode, Relationship oldRelationship) {
        move(fromNode, toNode, oldRelationship, null);
    }

    public void move(Node fromNode, Node toNode, Relationship oldRelationship, String excludeProperty) {

        Node newFromNode = oldRelationship.getStartNode().equals(fromNode)
                         ? toNode
                         : oldRelationship.getStartNode();

        Node newToNode = newFromNode.equals(toNode)
                       ? oldRelationship.getEndNode()
                       : toNode;

        Relationship newRelationship = newFromNode.createRelationshipTo(newToNode, oldRelationship.getType());

        relationshipPropertyCopier.copy(oldRelationship, newRelationship, excludeProperty);


        oldRelationship.delete();
    }

    public void setRelationshipPropertyCopier(RelationshipPropertyCopier relationshipPropertyCopier) {
        this.relationshipPropertyCopier = relationshipPropertyCopier;
    }
}
