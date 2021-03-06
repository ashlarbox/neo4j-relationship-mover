package org.ashlarbox.neo4j.relationship.move;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.test.TestGraphDatabaseFactory;

import java.util.HashMap;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.neo4j.graphdb.DynamicLabel.label;
import static org.neo4j.graphdb.DynamicRelationshipType.withName;

@RunWith(MockitoJUnitRunner.class)
public class RelationshipMover_UT {

    @Mock
    private RelationshipPropertyAdder relationshipPropertyAdder;

    @Mock
    private RelationshipPropertyCopier relationshipPropertyCopier;

    @InjectMocks
    private final RelationshipMover relationshipMover = new RelationshipMover();

    private GraphDatabaseService graphDb;

    private static final RelationshipType TEST = withName("TEST");

    private Node fromNode;
    private Node toNode;
    private Node otherNode;
    private Relationship oldRelationship;

    @Mock private HashMap<String, Object> options;
    private Transaction tx;

    @Before
    public void prepareTestDatabase()
    {
        graphDb = new TestGraphDatabaseFactory().newImpermanentDatabase();

        tx = graphDb.beginTx();
        fromNode = graphDb.createNode(label("FROM"));
        toNode = graphDb.createNode(label("TO"));
        otherNode = graphDb.createNode(label("OTHER"));
    }

    @After
    public void destroyTestDatabase()
    {
        tx.close();
    }

    @Test
    public void moverShouldCopyOutboundRelationshipFromOneNodeToAnother() {
        oldRelationship = fromNode.createRelationshipTo(otherNode, TEST);

        relationshipMover.move(fromNode, toNode, oldRelationship, options);

        Relationship newRelationship = toNode.getSingleRelationship(TEST, Direction.OUTGOING);

        assertThat(newRelationship.getOtherNode(toNode), is(otherNode));
        assertThat(fromNode.getSingleRelationship(TEST, Direction.OUTGOING), is(nullValue()));
    }

    @Test
    public void moverShouldCopyInboundRelationshipFromOneNodeToAnother() {
        oldRelationship = otherNode.createRelationshipTo(fromNode, TEST);

        relationshipMover.move(fromNode, toNode, oldRelationship, options);

        Relationship newRelationship = toNode.getSingleRelationship(TEST, Direction.INCOMING);

        assertThat(newRelationship.getOtherNode(toNode), is(otherNode));
        assertThat(fromNode.getSingleRelationship(TEST, Direction.INCOMING), is(nullValue()));
    }

    @Test
    public void moverShouldRunPropertyFunctions() {
        oldRelationship = fromNode.createRelationshipTo(otherNode, TEST);

        relationshipMover.move(fromNode, toNode, oldRelationship, options);

        Relationship newRelationship = toNode.getSingleRelationship(TEST, Direction.OUTGOING);

        verify(relationshipPropertyCopier).copy(oldRelationship, newRelationship, options);
        verify(relationshipPropertyAdder).add(newRelationship, options);
    }


}
