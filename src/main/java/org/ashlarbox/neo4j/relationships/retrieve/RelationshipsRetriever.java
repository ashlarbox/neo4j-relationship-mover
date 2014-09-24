package org.ashlarbox.neo4j.relationships.retrieve;

import com.google.common.collect.FluentIterable;
import org.ashlarbox.neo4j.relationships.retrieve.rule.RelationshipsHasPropertyValueRule;
import org.ashlarbox.neo4j.relationships.retrieve.rule.RelationshipsLimitSizeRule;
import org.ashlarbox.neo4j.relationships.retrieve.rule.RelationshipsWithLabelRule;
import org.ashlarbox.neo4j.relationships.retrieve.rule.RetrieveExcludeNodeRule;
import org.ashlarbox.neo4j.relationships.retrieve.rule.RetrieveForDirectionRule;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import java.util.HashMap;
import java.util.List;

public class RelationshipsRetriever {

    private RetrieveExcludeNodeRule retrieveExcludeNodeRule = new RetrieveExcludeNodeRule();
    private RetrieveForDirectionRule retrieveForDirectionRule = new RetrieveForDirectionRule();
    private RelationshipsHasPropertyValueRule relationshipsHasPropertyValueRule = new RelationshipsHasPropertyValueRule();
    private RelationshipsLimitSizeRule relationshipsLimitSizeRule = new RelationshipsLimitSizeRule();
    private RelationshipsWithLabelRule relationshipsWithLabelRule = new RelationshipsWithLabelRule();

    public List<Relationship> retrieve(Node sourceNode, Node excludeNode, HashMap<String, Object> options) {
        FluentIterable<Relationship> relationships = retrieveForDirectionRule.apply(sourceNode, options);
        relationships = relationshipsHasPropertyValueRule.apply(relationships, options);
        relationships = relationshipsWithLabelRule.apply(relationships, sourceNode, options);
        relationships = retrieveExcludeNodeRule.apply(relationships, excludeNode);
        relationships = relationshipsLimitSizeRule.apply(relationships, options);
        return relationships.toList();
    }

}
