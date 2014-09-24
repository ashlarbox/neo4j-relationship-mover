package org.ashlarbox.neo4j.relationships.retrieve.rule;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import java.util.HashMap;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static org.ashlarbox.neo4j.constants.OptionConstants.DIRECTION;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.neo4j.graphdb.Direction.BOTH;
import static org.neo4j.graphdb.Direction.OUTGOING;

@RunWith(MockitoJUnitRunner.class)
public class RelationshipsForDirectionRule_UT {

    private final RelationshipsForDirectionRule relationshipsForDirectionRule = new RelationshipsForDirectionRule();

    private HashMap<String, Object> options;
    private List<Relationship> relationships;

    private final Direction outgoing = OUTGOING;

    @Mock private Relationship incomingRelationship;
    @Mock private Relationship outgoingRelationship;
    @Mock private Node sourceNode;

    @Before
    public void initializeOptions() {
        options = newHashMap();
    }

    @Before
    public void initializeRelationships() {
        relationships = newArrayList(incomingRelationship, outgoingRelationship);
    }

    @Before
    public void mockSourceNodeCalls() {
        when(sourceNode.getRelationships(OUTGOING)).thenReturn(newArrayList(outgoingRelationship));
        when(sourceNode.getRelationships(BOTH)).thenReturn(newArrayList(incomingRelationship, outgoingRelationship));
    }

    @Test
    public void missingDirectionOptionShouldReturnForBothDirections() {
        List<Relationship> returnedRelationships = relationshipsForDirectionRule.apply(sourceNode, options).toList();
        assertThat(returnedRelationships.size(), is(relationships.size()));
        assertThat(returnedRelationships.containsAll(relationships), is(true));
    }

    @Test
    public void optionsHavingBothValuesShouldApplyFilter() {
        options.put(DIRECTION, outgoing);
        List<Relationship> returnedRelationships = relationshipsForDirectionRule.apply(sourceNode, options).toList();
        assertThat(returnedRelationships.size(), is(1));
        assertThat(returnedRelationships, hasItem(outgoingRelationship));
    }

}
