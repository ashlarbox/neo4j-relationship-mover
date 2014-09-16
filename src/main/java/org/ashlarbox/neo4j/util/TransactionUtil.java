package org.ashlarbox.neo4j.util;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;

public class TransactionUtil {

    private static GraphDatabaseService graphDatabaseService;

    private TransactionUtil() {

    }

    public void setGraphDatabaseService(GraphDatabaseService graphDatabaseService) {
        this.graphDatabaseService = graphDatabaseService;
    }

    public static Transaction startNewTransaction() {
        return graphDatabaseService.beginTx();
    }

    public static Transaction commitAndResetTransaction(Transaction tx) {
        tx.success();
        tx.close();
        return startNewTransaction();
    }
}
