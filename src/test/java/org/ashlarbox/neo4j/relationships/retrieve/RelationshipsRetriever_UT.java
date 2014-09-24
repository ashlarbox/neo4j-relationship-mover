package org.ashlarbox.neo4j.relationships.retrieve;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import org.ashlarbox.neo4j.relationships.retrieve.rule.RelationshipsExcludeWithNodesRule;
import org.ashlarbox.neo4j.relationships.retrieve.rule.RelationshipsForDirectionRule;
import org.ashlarbox.neo4j.relationships.retrieve.rule.RelationshipsHasPropertyValueRule;
import org.ashlarbox.neo4j.relationships.retrieve.rule.RelationshipsLimitSizeRule;
import org.ashlarbox.neo4j.relationships.retrieve.rule.RelationshipsWithLabelRule;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import java.util.HashMap;
import java.util.List;

import static com.google.common.collect.FluentIterable.from;
import static com.google.common.collect.Lists.newArrayList;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RelationshipsRetriever_UT {

    @Mock
    private RelationshipsExcludeWithNodesRule relationshipsExcludeWithNodesRule;

    @Mock
    private RelationshipsForDirectionRule relationshipsForDirectionRule;

    @Mock
    private RelationshipsHasPropertyValueRule relationshipsHasPropertyValueRule;

    @Mock
    private RelationshipsLimitSizeRule relationshipsLimitSizeRule;

    @Mock
    private RelationshipsWithLabelRule relationshipsWithLabelRule;

    @InjectMocks
    private final RelationshipsRetriever relationshipsRetriever = new RelationshipsRetriever();

    @Mock private Node sourceNode;
    @Mock private Node destinationNode;
    @Mock private HashMap<String, Object> options;

    private final ImmutableList<Relationship> expectedList = ImmutableList.copyOf(asList(mock(Relationship.class), mock(Relationship.class)));
    private final FluentIterable<Relationship> relationships = from(expectedList);

    @Before
    public void mockActions() {
        when(relationshipsForDirectionRule.apply(sourceNode, options)).thenReturn(relationships);
        when(relationshipsHasPropertyValueRule.apply(relationships, options)).thenReturn(relationships);
        when(relationshipsWithLabelRule.apply(relationships, sourceNode, options)).thenReturn(relationships);
        when(relationshipsExcludeWithNodesRule.apply(relationships, sourceNode, newArrayList(sourceNode, destinationNode))).thenReturn(relationships);
        when(relationshipsLimitSizeRule.apply(relationships, options)).thenReturn(relationships);
    }

    @Test
    public void retrieveRunsRulesInOrder() {
        InOrder inOrder = inOrder(relationshipsForDirectionRule,
                relationshipsHasPropertyValueRule,
                relationshipsWithLabelRule,
                relationshipsExcludeWithNodesRule,
                relationshipsLimitSizeRule);

        relationshipsRetriever.retrieve(sourceNode, destinationNode, options);

        inOrder.verify(relationshipsForDirectionRule).apply(sourceNode, options);
        inOrder.verify(relationshipsHasPropertyValueRule).apply(relationships, options);
        inOrder.verify(relationshipsWithLabelRule).apply(relationships, sourceNode, options);
        inOrder.verify(relationshipsExcludeWithNodesRule).apply(relationships, sourceNode, newArrayList(sourceNode, destinationNode));
        inOrder.verify(relationshipsLimitSizeRule).apply(relationships, options);
    }

    @Test
    public void retrieveReturnsExpectedList() {
        List<Relationship> returnedList = relationshipsRetriever.retrieve(sourceNode, destinationNode, options);

        assertThat(returnedList, is((List<Relationship>) expectedList));
    }

}
