package org.ashlarbox.neo4j.relationships.retrieve.rule;

import com.google.common.collect.FluentIterable;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;

import java.util.HashMap;

import static org.ashlarbox.neo4j.constants.OptionConstants.WITH_LABEL;
import static org.ashlarbox.neo4j.relationship.predicate.WithLabelPredicate.linkedWithLabel;

public class RetrieveWithLabelRule {

    public FluentIterable apply(FluentIterable iterable, Node sourceNode, HashMap<String, Object> options) {
        return options.containsKey(WITH_LABEL)
                ? iterable
                : iterable.filter(linkedWithLabel(sourceNode, (Label) options.get(WITH_LABEL)));
    }

}
