package org.ashlarbox.neo4j.relationships.move;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;

import java.util.HashMap;
import java.util.List;

import static com.google.common.collect.Iterables.partition;
import static org.ashlarbox.neo4j.constants.OptionConstants.COMMIT_SIZE;
import static org.ashlarbox.neo4j.relationship.move.RelationshipMover.moveRelationship;
import static org.ashlarbox.neo4j.relationships.retrieve.RelationshipsRetriever.retrieveRelationships;
import static org.ashlarbox.neo4j.util.TransactionUtil.commitAndResetTransaction;
import static org.ashlarbox.neo4j.util.TransactionUtil.startNewTransaction;

public class RelationshipsMover {


    public static void moveRelationships(Node fromNode, Node toNode, HashMap<String, Object> options) {

        int commitSize = (options.containsKey(COMMIT_SIZE)) ? (Integer) options.get(COMMIT_SIZE) : 1000;
        Transaction tx = startNewTransaction();

        try {
            List<Relationship> allRelationships = retrieveRelationships(fromNode, toNode, options);
            Iterable<List<Relationship>> partitions = partition(allRelationships, commitSize);

            for (List<Relationship> relationships : partitions) {
                for (Relationship relationship : relationships) {
                    moveRelationship(fromNode, toNode, relationship);
                }
                tx = commitAndResetTransaction(tx);
            }

        } catch (RuntimeException e) {
            tx.failure();
            throw e;

        } finally {
            tx.close();
        }

    }

}
