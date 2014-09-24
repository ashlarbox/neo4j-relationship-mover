package org.ashlarbox.neo4j.relationships.retrieve.rule;

import com.google.common.collect.FluentIterable;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import java.util.HashMap;

import static org.ashlarbox.neo4j.constants.OptionConstants.WITH_LABEL;
import static org.ashlarbox.neo4j.option.ValidateAndRetrieveOption.validateAndRetrieve;
import static org.ashlarbox.neo4j.relationship.predicate.WithLabelPredicate.linkedWithLabel;

public class RetrieveWithLabelRule {

    public FluentIterable<Relationship> apply(FluentIterable<Relationship> iterable, Node sourceNode, HashMap<String, Object> options) {
        Label label = (Label) validateAndRetrieve(options, WITH_LABEL, Label.class);
        return (label == null)
                ? iterable
                : iterable.filter(linkedWithLabel(sourceNode, label));
    }

}
