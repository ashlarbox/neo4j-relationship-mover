package org.ashlarbox.neo4j.relationships.retrieve;

import com.google.common.collect.FluentIterable;
import org.ashlarbox.neo4j.relationships.retrieve.rule.RelationshipsExcludeWithNodesRule;
import org.ashlarbox.neo4j.relationships.retrieve.rule.RelationshipsForDirectionRule;
import org.ashlarbox.neo4j.relationships.retrieve.rule.RelationshipsHasPropertyValueRule;
import org.ashlarbox.neo4j.relationships.retrieve.rule.RelationshipsLimitSizeRule;
import org.ashlarbox.neo4j.relationships.retrieve.rule.RelationshipsWithLabelRule;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import java.util.HashMap;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class RelationshipsRetriever {

    private RelationshipsExcludeWithNodesRule relationshipsExcludeWithNodesRule = new RelationshipsExcludeWithNodesRule();
    private RelationshipsForDirectionRule relationshipsForDirectionRule = new RelationshipsForDirectionRule();
    private RelationshipsHasPropertyValueRule relationshipsHasPropertyValueRule = new RelationshipsHasPropertyValueRule();
    private RelationshipsLimitSizeRule relationshipsLimitSizeRule = new RelationshipsLimitSizeRule();
    private RelationshipsWithLabelRule relationshipsWithLabelRule = new RelationshipsWithLabelRule();

    public List<Relationship> retrieve(Node sourceNode, Node destinationNode, HashMap<String, Object> options) {
        FluentIterable<Relationship> relationships = relationshipsForDirectionRule.apply(sourceNode, options);
        relationships = relationshipsHasPropertyValueRule.apply(relationships, options);
        relationships = relationshipsWithLabelRule.apply(relationships, sourceNode, options);
        relationships = relationshipsExcludeWithNodesRule.apply(relationships, sourceNode, newArrayList(sourceNode, destinationNode));
        relationships = relationshipsLimitSizeRule.apply(relationships, options);
        return relationships.toList();
    }

}
