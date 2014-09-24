package org.ashlarbox.neo4j.relationships.move;

import com.google.common.collect.Maps;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.test.TestGraphDatabaseFactory;

import static com.google.common.collect.Lists.newArrayList;
import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.neo4j.graphdb.Direction.INCOMING;
import static org.neo4j.graphdb.Direction.OUTGOING;
import static org.neo4j.graphdb.DynamicLabel.label;
import static org.neo4j.graphdb.DynamicRelationshipType.withName;

public class MoveAllRelationships_MT {

    private static final DynamicRelationshipType HAS_FRIEND = withName("HAS_FRIEND");
    private static final DynamicRelationshipType DRIVES = withName("DRIVES");
    private static final DynamicRelationshipType HELPS = withName("HELPS");
    private static final DynamicRelationshipType AFFECTS = withName("AFFECTS");
    private static final DynamicRelationshipType LOVES = withName("LOVES");
    private final RelationshipsMoveController controller = new RelationshipsMoveController();

    private GraphDatabaseService graphDb;

    private Node korben;
    private Node leeloo;
    private Node rubyRhod;
    private Node taxi;
    private Node water;
    private Node love;

    @Before
    public void prepareTestDatabase()
    {
        graphDb = new TestGraphDatabaseFactory().newImpermanentDatabase();
        controller.setGraphDatabaseService(graphDb);

        korben = createNode("Korben Dallas", "Character");
        leeloo = createNode("Leeloo Dallas", "Character");
        rubyRhod = createNode("Ruby Rhod", "Friend");
        taxi = createNode("Taxi", "Vehicle");
        water = createNode("Water", "Element");
        love = createNode("Love", "Feeling");

        createRelationship(korben, HAS_FRIEND, rubyRhod);
        createRelationship(korben, DRIVES, taxi);
        createRelationship(water, HELPS, korben);
        createRelationship(love, AFFECTS, korben);
        createRelationship(korben, LOVES, leeloo);
    }

    @Test
    public void controllerShouldMoveRelationships() {
        controller.move(korben, leeloo, Maps.<String, Object>newHashMap());

        Transaction tx = graphDb.beginTx();

        assertThat(newArrayList(leeloo.getRelationships()).size(), is(5));
        assertThat(leeloo.getSingleRelationship(HAS_FRIEND, OUTGOING).getEndNode(), is(rubyRhod));
        assertThat(leeloo.getSingleRelationship(DRIVES, OUTGOING).getEndNode(), is(taxi));
        assertThat(leeloo.getSingleRelationship(HELPS, INCOMING).getStartNode(), is(water));
        assertThat(leeloo.getSingleRelationship(AFFECTS, INCOMING).getStartNode(), is(love));
        assertThat(leeloo.getSingleRelationship(LOVES, INCOMING).getStartNode(), is(korben));

        assertThat(newArrayList(korben.getRelationships()).size(), is(1));
        assertThat(korben.getSingleRelationship(LOVES, OUTGOING).getEndNode(), is(leeloo));

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
