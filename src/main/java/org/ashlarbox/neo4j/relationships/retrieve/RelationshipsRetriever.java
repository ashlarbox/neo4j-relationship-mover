package org.ashlarbox.neo4j.relationships.retrieve;

import com.google.common.collect.FluentIterable;
import org.ashlarbox.neo4j.relationships.retrieve.rule.RetrieveExcludeNodeRule;
import org.ashlarbox.neo4j.relationships.retrieve.rule.RetrieveForDirectionRule;
import org.ashlarbox.neo4j.relationships.retrieve.rule.RetrieveHasPropertyRule;
import org.ashlarbox.neo4j.relationships.retrieve.rule.RetrieveLimitRule;
import org.ashlarbox.neo4j.relationships.retrieve.rule.RetrieveWithLabelRule;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import java.util.HashMap;
import java.util.List;

public class RelationshipsRetriever {

    private RetrieveExcludeNodeRule retrieveExcludeNodeRule = new RetrieveExcludeNodeRule();
    private RetrieveForDirectionRule retrieveForDirectionRule = new RetrieveForDirectionRule();
    private RetrieveHasPropertyRule retrieveHasPropertyRule = new RetrieveHasPropertyRule();
    private RetrieveLimitRule retrieveLimitRule = new RetrieveLimitRule();
    private RetrieveWithLabelRule retrieveWithLabelRule = new RetrieveWithLabelRule();

    public List<Relationship> retrieve(Node sourceNode, Node excludeNode, HashMap<String, Object> options) {
        FluentIterable<Relationship> relationships = retrieveForDirectionRule.apply(sourceNode, options);
        relationships = retrieveHasPropertyRule.apply(relationships, options);
        relationships = retrieveWithLabelRule.apply(relationships, sourceNode, options);
        relationships = retrieveExcludeNodeRule.apply(relationships, excludeNode);
        relationships = retrieveLimitRule.apply(relationships, options);
        return relationships.toList();
    }

}
