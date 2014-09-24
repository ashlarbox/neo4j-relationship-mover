package org.ashlarbox.neo4j.relationship.predicate;

import com.google.common.base.Predicate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import static com.google.common.collect.Lists.newArrayList;
import static org.ashlarbox.neo4j.relationship.predicate.NotWithNodePredicate.notWithNode;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NotWithNodePredicate_UT {

    @Mock private Relationship relationship;
    @Mock private Node sourceNode;
    @Mock private Node includeNode;
    @Mock private Node excludeNode;
    private Predicate<Relationship> predicate;

    @Before
    public void initializePredicate() {
        predicate = notWithNode(sourceNode, newArrayList(sourceNode, excludeNode));
    }

    @Test
    public void predicateShouldReturnTrueWhenItDoesNotHaveExcludeNode() {
        when(relationship.getOtherNode(sourceNode)).thenReturn(includeNode);
        assertThat(predicate.apply(relationship), is(true));
    }

    @Test
    public void predicateShouldReturnFalseWhenItHasExcludeNode() {
        when(relationship.getOtherNode(sourceNode)).thenReturn(excludeNode);
        assertThat(predicate.apply(relationship), is(false));
    }

    @Test
    public void predicateShouldReturnFalseWhenItHasSourceNode() {
        when(relationship.getOtherNode(sourceNode)).thenReturn(sourceNode);
        assertThat(predicate.apply(relationship), is(false));
    }

}
