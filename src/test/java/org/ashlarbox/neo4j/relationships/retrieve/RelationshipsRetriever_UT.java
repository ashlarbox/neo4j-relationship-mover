package org.ashlarbox.neo4j.relationships.retrieve;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import org.ashlarbox.neo4j.relationships.retrieve.rule.RetrieveExcludeNodeRule;
import org.ashlarbox.neo4j.relationships.retrieve.rule.RetrieveForDirectionRule;
import org.ashlarbox.neo4j.relationships.retrieve.rule.RetrieveHasPropertyRule;
import org.ashlarbox.neo4j.relationships.retrieve.rule.RetrieveLimitRule;
import org.ashlarbox.neo4j.relationships.retrieve.rule.RetrieveWithLabelRule;
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
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RelationshipsRetriever_UT {

    @Mock
    private RetrieveExcludeNodeRule retrieveExcludeNodeRule;

    @Mock
    private RetrieveForDirectionRule retrieveForDirectionRule;

    @Mock
    private RetrieveHasPropertyRule retrieveHasPropertyRule;

    @Mock
    private RetrieveLimitRule retrieveLimitRule;

    @Mock
    private RetrieveWithLabelRule retrieveWithLabelRule;

    @InjectMocks
    private final RelationshipsRetriever relationshipsRetriever = new RelationshipsRetriever();

    @Mock private Node sourceNode;
    @Mock private Node excludeNode;
    @Mock private HashMap<String, Object> options;

    private final ImmutableList<Relationship> expectedList = ImmutableList.copyOf(asList(mock(Relationship.class), mock(Relationship.class)));
    private final FluentIterable<Relationship> relationships = from(expectedList);

    @Before
    public void mockActions() {
        when(retrieveForDirectionRule.apply(sourceNode, options)).thenReturn(relationships);
        when(retrieveHasPropertyRule.apply(relationships, options)).thenReturn(relationships);
        when(retrieveWithLabelRule.apply(relationships, sourceNode, options)).thenReturn(relationships);
        when(retrieveExcludeNodeRule.apply(relationships, excludeNode)).thenReturn(relationships);
        when(retrieveLimitRule.apply(relationships, options)).thenReturn(relationships);
    }

    @Test
    public void retrieveRunsRulesInOrder() {
        InOrder inOrder = inOrder(retrieveForDirectionRule,
                                  retrieveHasPropertyRule,
                                  retrieveWithLabelRule,
                                  retrieveExcludeNodeRule,
                                  retrieveLimitRule);

        relationshipsRetriever.retrieve(sourceNode, excludeNode, options);

        inOrder.verify(retrieveForDirectionRule).apply(sourceNode, options);
        inOrder.verify(retrieveHasPropertyRule).apply(relationships, options);
        inOrder.verify(retrieveWithLabelRule).apply(relationships, sourceNode, options);
        inOrder.verify(retrieveExcludeNodeRule).apply(relationships, excludeNode);
        inOrder.verify(retrieveLimitRule).apply(relationships, options);
    }

    @Test
    public void retrieveReturnsExpectedList() {
        List<Relationship> returnedList = relationshipsRetriever.retrieve(sourceNode, excludeNode, options);

        assertThat(returnedList, is((List<Relationship>) expectedList));
    }

}
