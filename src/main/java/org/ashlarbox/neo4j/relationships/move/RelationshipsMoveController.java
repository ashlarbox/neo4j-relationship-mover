package org.ashlarbox.neo4j.relationships.move;

import org.ashlarbox.neo4j.relationships.retrieve.RelationshipsRetriever;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import java.util.HashMap;
import java.util.List;

import static com.google.common.collect.Iterables.partition;
import static org.ashlarbox.neo4j.constants.DefaultConstants.DEFAULT_COMMIT_SIZE;
import static org.ashlarbox.neo4j.constants.OptionConstants.COMMIT_SIZE;
import static org.ashlarbox.neo4j.option.ValidateAndRetrieveOption.validateAndRetrieve;

public class RelationshipsMoveController {

    private GraphDatabaseService graphDatabaseService;
    private RelationshipsMover relationshipsMover = new RelationshipsMover();
    private RelationshipsRetriever relationshipsRetriever = new RelationshipsRetriever();

    public void move(Node fromNode, Node toNode, HashMap<String, Object> options) {

        List<Relationship> allRelationships = relationshipsRetriever.retrieve(graphDatabaseService, fromNode, toNode, options);

        Integer commitSize = (Integer) validateAndRetrieve(options, COMMIT_SIZE, Integer.class);
        commitSize = commitSize != null ? commitSize : DEFAULT_COMMIT_SIZE;

        for (List<Relationship> relationships : partition(allRelationships, commitSize)) {
            relationshipsMover.move(graphDatabaseService, fromNode, toNode, relationships, options);
        }

    }

    public void setGraphDatabaseService(GraphDatabaseService graphDatabaseService) {
        this.graphDatabaseService = graphDatabaseService;
    }
}
