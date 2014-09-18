package org.ashlarbox.neo4j.util;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import static org.ashlarbox.neo4j.util.RelationshipUtil.hasNode;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RelationshipUtil_UT {

    @Mock private Relationship relationship;
    @Mock private Node fromNode;
    @Mock private Node toNode;
    @Mock private Node otherNode;

    @Before
    public void defaultNodesOnRelationship() {
        when(relationship.getNodes()).thenReturn(new Node[]{fromNode, toNode});
    }

    @Test
    public void hasNodeReturnsTrueWhenFoundOnRelationship() {
        assertThat(hasNode(relationship, fromNode), is(true));
    }

    @Test
    public void hasNodeReturnsFalseWhenNotFoundOnRelationship() {
        assertThat(hasNode(relationship, otherNode), is(false));
    }
}
