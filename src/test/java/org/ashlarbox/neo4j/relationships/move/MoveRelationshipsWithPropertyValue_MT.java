package org.ashlarbox.neo4j.relationships.move;

import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.test.TestGraphDatabaseFactory;

import java.util.HashMap;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.ashlarbox.neo4j.constants.OptionConstants.EXCLUDE_NEW_PROPERTY;
import static org.ashlarbox.neo4j.constants.OptionConstants.HAS_PROPERTY_KEY;
import static org.ashlarbox.neo4j.constants.OptionConstants.HAS_PROPERTY_VALUE;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.neo4j.graphdb.Direction.INCOMING;
import static org.neo4j.graphdb.Direction.OUTGOING;
import static org.neo4j.graphdb.DynamicLabel.label;
import static org.neo4j.graphdb.DynamicRelationshipType.withName;

public class MoveRelationshipsWithPropertyValue_MT {

    private static final RelationshipType IS_NEIGHBORHOOD_OF = withName("IS_NEIGHBORHOOD_OF");
    private static final RelationshipType LOCATED_IN = withName("LOCATED_IN");
    private static final RelationshipType LIVES_IN = withName("LIVES_IN");
    private static final RelationshipType HOLDS = withName("HOLDS");
    private static final Label CITY_LABEL = label("City");
    private static final Label NEIGHBORHOOD_LABEL = label("Neighborhood");
    private static final Label PLACE_LABEL = label("Place");
    private static final Label PERSON_LABEL = label("Person");
    private static final Label EVENT_LABEL = label("Event");
    private static final String SOULARD = "Soulard";
    private static final String THE_HILL = "The Hill";
    private static final String NEIGHBORHOOD = "neighborhood";

    private final RelationshipsMoveController relationshipsMoveController = new RelationshipsMoveController();

    private GraphDatabaseService graphDb;

    private Node stLouis;

    private Node soulard;
    private Node anheuserBusch;
    private Node gussie;
    private Node mardiGras;

    private Node theHill;
    private Node stAmbrose;
    private Node yogiBerra;
    private Node bocce;

    @Before
    public void prepareTestDatabase()
    {
        graphDb = new TestGraphDatabaseFactory().newImpermanentDatabase();
        relationshipsMoveController.setGraphDatabaseService(graphDb);

        stLouis = createNode("St. Louis", CITY_LABEL);

        soulard = createNode(SOULARD, NEIGHBORHOOD_LABEL);
        anheuserBusch = createNode("Anheuser Busch Brewery", PLACE_LABEL);
        gussie = createNode("August Busch III", PERSON_LABEL);
        mardiGras = createNode("Mardi Gras Festival", EVENT_LABEL);

        theHill = createNode(THE_HILL, NEIGHBORHOOD_LABEL);
        stAmbrose = createNode("St. Ambrose", PLACE_LABEL);
        yogiBerra = createNode("Yogi Berra", PERSON_LABEL);
        bocce = createNode("Annual Bocce Tournament", EVENT_LABEL);

        createRelationship(soulard, IS_NEIGHBORHOOD_OF, stLouis, null);
        createRelationship(anheuserBusch, LOCATED_IN, stLouis, SOULARD);
        createRelationship(gussie, LIVES_IN, stLouis, SOULARD);
        createRelationship(stLouis, HOLDS, mardiGras, SOULARD);

        createRelationship(theHill, IS_NEIGHBORHOOD_OF, stLouis, null);
        createRelationship(stAmbrose, LOCATED_IN, stLouis, THE_HILL);
        createRelationship(yogiBerra, LIVES_IN, stLouis, THE_HILL);
        createRelationship(stLouis, HOLDS, bocce, THE_HILL);
    }

    @Test
    public void controllerShouldMoveRelationships() {
        HashMap<String, Object> options = newHashMap();

        options.put(HAS_PROPERTY_KEY, NEIGHBORHOOD);
        options.put(HAS_PROPERTY_VALUE, SOULARD);
        options.put(EXCLUDE_NEW_PROPERTY, NEIGHBORHOOD);
        relationshipsMoveController.move(stLouis, soulard, options);

        options.put(HAS_PROPERTY_VALUE, THE_HILL);
        relationshipsMoveController.move(stLouis, theHill, options);

        Transaction tx = graphDb.beginTx();

        assertThat(newArrayList(soulard.getRelationships()).size(), is(4));
        assertThat(soulard.getSingleRelationship(IS_NEIGHBORHOOD_OF, OUTGOING).getEndNode(), is(stLouis));
        assertThat(anheuserBusch.getSingleRelationship(LOCATED_IN, OUTGOING).getEndNode(), is(soulard));
        assertThat(gussie.getSingleRelationship(LIVES_IN, OUTGOING).getEndNode(), is(soulard));
        assertThat(mardiGras.getSingleRelationship(HOLDS, INCOMING).getStartNode(), is(soulard));

        assertThat(newArrayList(theHill.getRelationships()).size(), is(4));
        assertThat(theHill.getSingleRelationship(IS_NEIGHBORHOOD_OF, OUTGOING).getEndNode(), is(stLouis));
        assertThat(stAmbrose.getSingleRelationship(LOCATED_IN, OUTGOING).getEndNode(), is(theHill));
        assertThat(yogiBerra.getSingleRelationship(LIVES_IN, OUTGOING).getEndNode(), is(theHill));
        assertThat(bocce.getSingleRelationship(HOLDS, INCOMING).getStartNode(), is(theHill));

        assertThat(newArrayList(stLouis.getRelationships()).size(), is(2));

        tx.close();
    }

    private Node createNode(String name, Label label) {
        Node node = null;
        Transaction tx = graphDb.beginTx();
        try {
            node = graphDb.createNode();
            node.addLabel(label);
            node.setProperty("name", name);
            tx.success();
            System.out.println(format("%s Node[%d] '%s' created", label.name(), node.getId(), name));
        } catch (Exception e) {
            fail(format("Cannot create node %s created with label %s ", name, label.name()));
            tx.failure();
        } finally {
            tx.close();
        }
        return node;
    }

    private void createRelationship(Node fromNode, RelationshipType type, Node toNode, String neighborhood) {
        Transaction tx = graphDb.beginTx();
        try {
            Relationship relationship = fromNode.createRelationshipTo(toNode, type);
            if (isNotBlank(neighborhood)) {
                relationship.setProperty(NEIGHBORHOOD, neighborhood);
            }

            tx.success();
            System.out.println(format("Relationship [%d] (%s)-[:%s]->(%s) created",
                    relationship.getId(),
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
