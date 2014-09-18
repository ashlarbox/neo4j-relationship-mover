package org.ashlarbox.neo4j.relationship.predicate;

import com.google.common.base.Predicate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.ashlarbox.neo4j.relationship.predicate.WithLabelPredicate.linkedWithLabel;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.neo4j.graphdb.DynamicLabel.label;

@RunWith(MockitoJUnitRunner.class)
public class WithLabelPredicate_UT {

    @Mock private Relationship relationship;
    @Mock private Node sourceNode;
    @Mock private Node otherNode;
    private final Label testLabel = label(randomAlphabetic(8));
    private Predicate<Relationship> predicate;

    @Before
    public void setup() {
        predicate = linkedWithLabel(sourceNode, testLabel);
        when(relationship.getOtherNode(sourceNode)).thenReturn(otherNode);
    }

    @Test
    public void predicateShouldReturnTrueWhenOtherNodeHasLabel() {
        when(otherNode.hasLabel(testLabel)).thenReturn(true);
        assertThat(predicate.apply(relationship), is(true));
    }

    @Test
    public void predicateShouldReturnFalseWhenOtherNodeDoesNotHaveLabel() {
        when(otherNode.hasLabel(testLabel)).thenReturn(false);
        assertThat(predicate.apply(relationship), is(false));
    }

}
