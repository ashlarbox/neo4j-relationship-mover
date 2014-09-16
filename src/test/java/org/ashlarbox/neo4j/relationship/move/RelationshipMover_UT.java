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

import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.ashlarbox.neo4j.relationship.move.RelationshipMover.moveRelationship;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
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
        moveRelationship(fromNode, toNode, oldRelationship);
        tx.success();

        Relationship relationship = toNode.getSingleRelationship(TEST, Direction.OUTGOING);
        assertThat(relationship.getOtherNode(toNode), is(otherNode));

        assertThat(fromNode.getSingleRelationship(TEST, Direction.OUTGOING), is(nullValue()));
    }

    @Test
    public void moverShouldCopyInboundRelationshipFromOneNodeToAnother() {
        oldRelationship = otherNode.createRelationshipTo(fromNode, TEST);

        Transaction tx = graphDb.beginTx();
        moveRelationship(fromNode, toNode, oldRelationship);
        tx.success();

        Relationship relationship = toNode.getSingleRelationship(TEST, Direction.INCOMING);
        assertThat(relationship.getOtherNode(toNode), is(otherNode));

        assertThat(fromNode.getSingleRelationship(TEST, Direction.INCOMING), is(nullValue()));
    }

    @Test
    public void moverShouldCopyPropertiesToNewRelationship() {
        oldRelationship = fromNode.createRelationshipTo(otherNode, TEST);

        Map<String, String> propertyMap = newHashMap();
        int propertiesToAdd = nextInt(0, 5);
        for (int i = 0; i <= propertiesToAdd; i++) {
            String key = randomAlphabetic(8);
            String value = randomAlphanumeric(10);
            System.out.println("Property: " + i + "   Key: " + key + "   Value: " + value);
            propertyMap.put(key, value);
            oldRelationship.setProperty(key, value);
        }

        Transaction tx = graphDb.beginTx();
        moveRelationship(fromNode, toNode, oldRelationship);
        tx.success();

        Relationship newRelationship = toNode.getSingleRelationship(TEST, Direction.OUTGOING);
        for (Map.Entry<String, String> entry : propertyMap.entrySet()) {
            assertThat((String) newRelationship.getProperty(entry.getKey()), is(entry.getValue()));
        }
    }

}
