package org.ashlarbox.neo4j.relationships.move;

import org.ashlarbox.neo4j.relationships.retrieve.RelationshipsRetriever;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import java.util.HashMap;
import java.util.List;

import static com.google.common.collect.Iterables.partition;
import static org.ashlarbox.neo4j.constants.OptionConstants.COMMIT_SIZE;

public class MoveRelationships {

    private RelationshipsMover relationshipsMover = new RelationshipsMover();
    private RelationshipsRetriever relationshipsRetriever = new RelationshipsRetriever();

    public void move(Node fromNode, Node toNode, HashMap<String, Object> options) {

        List<Relationship> allRelationships = relationshipsRetriever.retrieve(fromNode, toNode, options);

        int commitSize = (options.containsKey(COMMIT_SIZE)) ? (Integer) options.get(COMMIT_SIZE) : 1000;

        for (List<Relationship> relationships : partition(allRelationships, commitSize)) {
            relationshipsMover.move(fromNode, toNode, relationships);
        }

    }


    public void setRelationshipsMover(RelationshipsMover relationshipsMover) {
        this.relationshipsMover = relationshipsMover;
    }

    public void setRelationshipsRetriever(RelationshipsRetriever relationshipsRetriever) {
        this.relationshipsRetriever = relationshipsRetriever;
    }
}
