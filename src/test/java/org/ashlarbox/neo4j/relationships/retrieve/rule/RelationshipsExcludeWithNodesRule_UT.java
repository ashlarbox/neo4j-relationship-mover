package org.ashlarbox.neo4j.relationships.retrieve.rule;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import java.util.List;

import static com.google.common.collect.FluentIterable.from;
import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RelationshipsExcludeWithNodesRule_UT {

    private final RelationshipsExcludeWithNodesRule relationshipsExcludeWithNodesRule = new RelationshipsExcludeWithNodesRule();

    private List<Relationship> relationships;

    @Mock private Relationship goodRelationship;
    @Mock private Relationship relationshipWithDestinationNode;
    @Mock private Relationship relationshipWithSourceNode;

    @Mock private Node sourceNode;
    @Mock private Node destinationNode;
    @Mock private Node otherNode;

    @Before
    public void initializeRelationships() {
        relationships = newArrayList(goodRelationship, relationshipWithDestinationNode, relationshipWithSourceNode);

        when(goodRelationship.getOtherNode(sourceNode)).thenReturn(otherNode);
        when(relationshipWithDestinationNode.getOtherNode(sourceNode)).thenReturn(destinationNode);
        when(relationshipWithSourceNode.getOtherNode(sourceNode)).thenReturn(sourceNode);
    }

    @Test
    public void ruleShouldOnlyReturnRelationshipWithOtherNode() {
        List<Relationship> returnedRelationships = relationshipsExcludeWithNodesRule.apply(from(relationships), sourceNode, newArrayList(sourceNode, destinationNode)).toList();
        assertThat(returnedRelationships.size(), is(1));
        assertThat(returnedRelationships, hasItem(goodRelationship));

    }


}
