package org.ashlarbox.neo4j.relationships.move;

import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.test.TestGraphDatabaseFactory;

import java.util.HashMap;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static java.lang.String.format;
import static org.ashlarbox.neo4j.constants.OptionConstants.WITH_LABEL;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.neo4j.graphdb.DynamicLabel.label;
import static org.neo4j.graphdb.DynamicRelationshipType.withName;

public class MoveRelationshipsWithLabel_MT {

    private static final RelationshipType HAS_FRIEND = withName("HAS_FRIEND");
    private static final RelationshipType DRIVES = withName("DRIVES");
    private static final RelationshipType HELPS = withName("HELPS");
    private static final RelationshipType AFFECTS = withName("AFFECTS");
    private static final RelationshipType LOVES = withName("LOVES");
    private static final String ELEMENT = "Element";
    private static final String CHARACTER = "Character";
    private final RelationshipsMoveController relationshipsMoveController = new RelationshipsMoveController();

    private GraphDatabaseService graphDb;

    private Node korben;
    private Node leeloo;
    private Node rubyRhod;
    private Node taxi;
    private Node water;
    private Node earth;
    private Node wind;
    private Node fire;
    private Node love;

    @Before
    public void prepareTestDatabase()
    {
        graphDb = new TestGraphDatabaseFactory().newImpermanentDatabase();
        relationshipsMoveController.setGraphDatabaseService(graphDb);

        korben = createNode("Korben Dallas", CHARACTER);
        leeloo = createNode("Leeloo Dallas", CHARACTER);
        rubyRhod = createNode("Ruby Rhod", "Friend");
        taxi = createNode("Taxi", "Vehicle");
        water = createNode("Water", ELEMENT);
        earth = createNode("Earth", ELEMENT);
        wind = createNode("Wind", ELEMENT);
        fire = createNode("Fire", ELEMENT);
        love = createNode("Love", "Feeling");

        createRelationship(korben, HAS_FRIEND, rubyRhod);
        createRelationship(korben, DRIVES, taxi);
        createRelationship(water, HELPS, korben);
        createRelationship(earth, HELPS, korben);
        createRelationship(wind, HELPS, korben);
        createRelationship(korben, HELPS, fire);   //note this is reversed intentionally
        createRelationship(love, AFFECTS, korben);
        createRelationship(korben, LOVES, leeloo);
    }

    @Test
    public void controllerShouldMoveRelationshipsWithLabelRegardlessOfDirection() {
        HashMap<String, Object> options = newHashMap();
        options.put(WITH_LABEL, label(ELEMENT));

        relationshipsMoveController.move(korben, leeloo, options);

        Transaction tx = graphDb.beginTx();

        List<Node> elementNodes = newArrayList();
        for (Relationship relationship : leeloo.getRelationships(HELPS)) {
            elementNodes.add(relationship.getOtherNode(leeloo));
        }
        assertThat(elementNodes.size(), is(4));
        assertThat(elementNodes, hasItems(earth, wind, fire, water));

        tx.close();
    }

    private Node createNode(String name, String labelName) {
        Node node = null;
        Transaction tx = graphDb.beginTx();
        try {
            node = graphDb.createNode();
            node.addLabel(label(labelName));
            node.setProperty("name", name);
            tx.success();
            System.out.println(format("Node %s[%d] created with label %s ", name, node.getId(), labelName));
        } catch (Exception e) {
            fail(format("Cannot create node %s created with label %s ", name, labelName));
            tx.failure();
        } finally {
            tx.close();
        }
        return node;
    }

    private void createRelationship(Node fromNode, RelationshipType type, Node toNode) {
        Transaction tx = graphDb.beginTx();
        try {
            fromNode.createRelationshipTo(toNode, type);
            tx.success();
            System.out.println(format("Relationship (%s)-[:%s]->(%s) created",
                    fromNode.getProperty("name"),
                    type.name(),
                    toNode.getProperty("name")));
        } catch (Exception e) {
            fail(format( "Cannot create relationship (%s)-[:%s]->(%s) created",
                    fromNode.getProperty("name"),
                    type.name(),
                    toNode.getProperty("name")));
            tx.failure();
        } finally {
            tx.close();
        }
    }
}
