package org.ashlarbox.neo4j.relationship.predicate;

import com.google.common.base.Predicate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import static org.ashlarbox.neo4j.relationship.predicate.NotWithNodePredicate.notWithNode;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NotWithNodePredicate_UT {

    @Mock private Relationship relationship;
    @Mock private Node includeNode;
    @Mock private Node excludeNode;
    private Predicate<Relationship> predicate;

    @Before
    public void initializePredicate() {
        predicate = notWithNode(excludeNode);
    }

    @Test
    public void predicateShouldReturnTrueWhenItDoesNotHaveExcludeNode() {
        when(relationship.getNodes()).thenReturn(new Node[] { includeNode });
        assertThat(predicate.apply(relationship), is(true));
    }

    @Test
    public void predicateShouldReturnFalseWhenItHasExcludeNode() {
        when(relationship.getNodes()).thenReturn(new Node[] { includeNode, excludeNode });
        assertThat(predicate.apply(relationship), is(false));
    }

}
