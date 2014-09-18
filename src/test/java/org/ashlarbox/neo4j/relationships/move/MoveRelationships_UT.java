package org.ashlarbox.neo4j.relationships.move;

import org.ashlarbox.neo4j.relationships.retrieve.RelationshipsRetriever;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import java.util.HashMap;
import java.util.List;

import static com.google.common.collect.Iterables.partition;
import static com.google.common.collect.Lists.newArrayList;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.ashlarbox.neo4j.constants.DefaultConstants.DEFAULT_COMMIT_SIZE;
import static org.ashlarbox.neo4j.constants.OptionConstants.COMMIT_SIZE;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MoveRelationships_UT {

    @Mock
    private RelationshipsMover relationshipsMover;

    @Mock
    private RelationshipsRetriever relationshipsRetriever;

    @InjectMocks
    private final MoveRelationships moveRelationships = new MoveRelationships();

    @Mock private Node fromNode;
    @Mock private Node toNode;
    @Mock private HashMap<String, Object> options;

    private List<Relationship> relationships;

    @Before
    public void setup() {
        relationships = newArrayList();
        for (int i = 0; i < DEFAULT_COMMIT_SIZE; i++) {
            relationships.add(mock(Relationship.class));
        }

        when(relationshipsRetriever.retrieve(fromNode, toNode, options)).thenReturn(relationships);
    }

    @Test
    public void lessEqualRelationshipsToCommitSizeShouldCallRelationshipMoverOnce() {
        when(options.containsKey(COMMIT_SIZE)).thenReturn(false);

        moveRelationships.move(fromNode, toNode, options);

        verify(relationshipsMover).move(fromNode, toNode, relationships, options);
        verifyNoMoreInteractions(relationshipsMover);
    }

    @Test
    public void defaultPassShouldOnlyRunOneMoveCall() {
        int commitSize = nextInt(10, 51);

        when(options.containsKey(COMMIT_SIZE)).thenReturn(true);
        when(options.get(COMMIT_SIZE)).thenReturn(commitSize);

        moveRelationships.move(fromNode, toNode, options);

        Iterable<List<Relationship>> partitions = partition(relationships, commitSize);
        for (List<Relationship> partition : partitions) {
            verify(relationshipsMover).move(fromNode, toNode, partition, options);
        }
        verifyNoMoreInteractions(relationshipsMover);
    }
}
