package org.ashlarbox.neo4j.relationships.retrieve.rule;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import java.util.HashMap;
import java.util.List;

import static com.google.common.collect.FluentIterable.from;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static org.ashlarbox.neo4j.constants.OptionConstants.WITH_LABEL;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RelationshipsWithLabelRule_UT {

    private final RelationshipsWithLabelRule relationshipsWithLabelRule = new RelationshipsWithLabelRule();

    private HashMap<String, Object> options;
    private List<Relationship> relationships;

    @Mock private Label label;
    @Mock private Relationship goodRelationship;
    @Mock private Relationship otherRelationship;
    @Mock private Node sourceNode;
    @Mock private Node goodNode;
    @Mock private Node otherNode;

    @Before
    public void initializeOptions() {
        options = newHashMap();
    }

    @Before
    public void initializeRelationships() {
        relationships = newArrayList(goodRelationship, otherRelationship);
    }

    @Before
    public void mockGoodRelationship() {
        when(goodRelationship.getOtherNode(sourceNode)).thenReturn(goodNode);
        when(goodNode.hasLabel(label)).thenReturn(true);
    }

    @Before
    public void mockOtherRelationship() {
        when(otherRelationship.getOtherNode(sourceNode)).thenReturn(otherNode);
        when(otherNode.hasLabel(label)).thenReturn(false);
    }

    @Test
    public void missingLabelOptionShouldNotApplyFilter() {
        List<Relationship> returnedRelationships = relationshipsWithLabelRule.apply(from(relationships), sourceNode, options).toList();
        assertThat(returnedRelationships.size(), is(relationships.size()));
        assertThat(returnedRelationships.containsAll(relationships), is(true));
    }

    @Test
    public void optionsHavingBothValuesShouldApplyFilter() {
        options.put(WITH_LABEL, label);
        List<Relationship> returnedRelationships = relationshipsWithLabelRule.apply(from(relationships), sourceNode, options).toList();
        assertThat(returnedRelationships.size(), is(1));
        assertThat(returnedRelationships, hasItem(goodRelationship));
    }

}
