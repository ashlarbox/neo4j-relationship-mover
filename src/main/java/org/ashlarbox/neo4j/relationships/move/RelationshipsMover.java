package org.ashlarbox.neo4j.relationships.move;

import org.ashlarbox.neo4j.relationship.move.RelationshipMover;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;

import java.util.HashMap;
import java.util.List;

class RelationshipsMover {

    private GraphDatabaseService graphDatabaseService;
    private RelationshipMover relationshipMover = new RelationshipMover();

    void move(Node fromNode, Node toNode, List<Relationship> relationships, HashMap<String, Object> options) {
        Transaction tx = graphDatabaseService.beginTx();

        try {
            for (Relationship relationship : relationships) {
                relationshipMover.move(fromNode, toNode, relationship, options);
            }
            tx.success();

        } catch (RuntimeException e) {
            tx.failure();
            throw e;

        } finally {
            tx.close();
        }
    }

}
