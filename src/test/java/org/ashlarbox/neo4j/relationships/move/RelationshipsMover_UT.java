package org.ashlarbox.neo4j.relationships.move;

import org.ashlarbox.neo4j.relationship.move.RelationshipMover;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;

import java.util.HashMap;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.junit.rules.ExpectedException.none;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RelationshipsMover_UT {

    @Mock
    private GraphDatabaseService graphDatabaseService;

    @Mock
    private RelationshipMover relationshipMover;

    @InjectMocks
    private final RelationshipsMover relationshipsMover = new RelationshipsMover();

    @Mock private Node fromNode;
    @Mock private Node toNode;
    @Mock private HashMap<String, Object> options;
    @Mock private Transaction tx;

    private List<Relationship> relationships;

    @Rule
    public ExpectedException expectedException = none();

    @Before
    public void setup() {
        relationships = newArrayList();
        int numOfRelationships = nextInt(1, 5);
        for (int i=0; i<numOfRelationships; i++) {
            relationships.add(mock(Relationship.class));
        }

        when(graphDatabaseService.beginTx()).thenReturn(tx);
    }

    @Test
    public void successfulCallMovesEachRelationship() {
        relationshipsMover.move(fromNode, toNode, relationships, options);

        for (Relationship relationship : relationships) {
            verify(relationshipMover).move(fromNode, toNode, relationship, options);
        }
        verifyNoMoreInteractions(relationshipMover);
    }

    @Test
    public void successfulCallCompletesTransaction() {
        relationshipsMover.move(fromNode, toNode, relationships, options);

        verify(tx).success();
        verify(tx).close();
        verifyNoMoreInteractions(tx);
    }

    @Test
    public void exceptionFailsTransactionAndThrowsException() {
        RuntimeException thrownException = mock(RuntimeException.class);
        String errorMessage = randomAlphanumeric(14);
        when(thrownException.getMessage()).thenReturn(errorMessage);

        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage(errorMessage);

        doThrow(thrownException).when(relationshipMover).move(fromNode, toNode, relationships.get(0), options);

        relationshipsMover.move(fromNode, toNode, relationships, options);

        verify(tx).failure();
        verify(tx).close();
        verifyNoMoreInteractions(tx);
    }
}
