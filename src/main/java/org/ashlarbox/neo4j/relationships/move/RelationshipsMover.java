package org.ashlarbox.neo4j.relationships.move;

import org.ashlarbox.neo4j.relationship.move.RelationshipMover;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;

import java.util.List;

public class RelationshipsMover {

    private GraphDatabaseService graphDatabaseService;
    private RelationshipMover relationshipMover = new RelationshipMover();

    protected void move(Node fromNode, Node toNode, List<Relationship> relationships) {
        Transaction tx = graphDatabaseService.beginTx();

        try {
            for (Relationship relationship : relationships) {
                relationshipMover.move(fromNode, toNode, relationship);
            }
            tx.success();

        } catch (RuntimeException e) {
            tx.failure();
            throw e;

        } finally {
            tx.close();
        }
    }

    public void setGraphDatabaseService(GraphDatabaseService graphDatabaseService) {
        this.graphDatabaseService = graphDatabaseService;
    }

    public void setRelationshipMover(RelationshipMover relationshipMover) {
        this.relationshipMover = relationshipMover;
    }
}
