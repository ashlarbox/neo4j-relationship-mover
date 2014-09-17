package org.ashlarbox.neo4j.relationship.move;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.test.TestGraphDatabaseFactory;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.neo4j.graphdb.DynamicLabel.label;
import static org.neo4j.graphdb.DynamicRelationshipType.withName;

@RunWith(MockitoJUnitRunner.class)
public class RelationshipMover_UT {

    protected GraphDatabaseService graphDb;
    private static final RelationshipType TEST = withName("TEST");

    private Node fromNode;
    private Node toNode;
    private Node otherNode;
    private Relationship oldRelationship;

    private final RelationshipMover relationshipMover = new RelationshipMover();

    @Mock private RelationshipPropertyCopier relationshipPropertyCopier;

    @Before
    public void injectMockCopier() {
        relationshipMover.setRelationshipPropertyCopier(relationshipPropertyCopier);
    }

    @Before
    public void prepareTestDatabase()
    {
        graphDb = new TestGraphDatabaseFactory().newImpermanentDatabase();

        Transaction tx = graphDb.beginTx();
        fromNode = graphDb.createNode(label("FROM"));
        toNode = graphDb.createNode(label("TO"));
        otherNode = graphDb.createNode(label("OTHER"));
        tx.success();
    }

    @After
    public void destroyTestDatabase()
    {
        graphDb.shutdown();
    }

    @Test
    public void moverShouldCopyOutboundRelationshipFromOneNodeToAnother() {
        oldRelationship = fromNode.createRelationshipTo(otherNode, TEST);

        Transaction tx = graphDb.beginTx();
        relationshipMover.move(fromNode, toNode, oldRelationship);
        tx.success();

        Relationship newRelationship = toNode.getSingleRelationship(TEST, Direction.OUTGOING);
        assertThat(newRelationship.getOtherNode(toNode), is(otherNode));

        assertThat(fromNode.getSingleRelationship(TEST, Direction.OUTGOING), is(nullValue()));

        verify(relationshipPropertyCopier).copy(oldRelationship, newRelationship, null);
    }

    @Test
    public void moverShouldCopyInboundRelationshipFromOneNodeToAnother() {
        oldRelationship = otherNode.createRelationshipTo(fromNode, TEST);

        Transaction tx = graphDb.beginTx();
        relationshipMover.move(fromNode, toNode, oldRelationship);
        tx.success();

        Relationship relationship = toNode.getSingleRelationship(TEST, Direction.INCOMING);
        assertThat(relationship.getOtherNode(toNode), is(otherNode));

        assertThat(fromNode.getSingleRelationship(TEST, Direction.INCOMING), is(nullValue()));
    }

}
