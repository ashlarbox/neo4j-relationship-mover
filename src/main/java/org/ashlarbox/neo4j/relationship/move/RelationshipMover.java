package org.ashlarbox.neo4j.relationship.move;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import java.util.HashMap;

import static org.ashlarbox.neo4j.constants.OptionConstants.EXCLUDE_NEW_PROPERTY;

public class RelationshipMover {

    private RelationshipPropertyCopier relationshipPropertyCopier = new RelationshipPropertyCopier();

    public void move(Node fromNode, Node toNode, Relationship oldRelationship, HashMap<String, Object> options) {

        Node newFromNode = oldRelationship.getStartNode().equals(fromNode)
                         ? toNode
                         : oldRelationship.getStartNode();

        Node newToNode = newFromNode.equals(toNode)
                       ? oldRelationship.getEndNode()
                       : toNode;

        Relationship newRelationship = newFromNode.createRelationshipTo(newToNode, oldRelationship.getType());

        relationshipPropertyCopier.copy(oldRelationship, newRelationship, (String) options.get(EXCLUDE_NEW_PROPERTY));


        oldRelationship.delete();
    }

    public void setRelationshipPropertyCopier(RelationshipPropertyCopier relationshipPropertyCopier) {
        this.relationshipPropertyCopier = relationshipPropertyCopier;
    }
}
