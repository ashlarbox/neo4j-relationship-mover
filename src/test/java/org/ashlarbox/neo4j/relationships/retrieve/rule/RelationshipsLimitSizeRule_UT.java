package org.ashlarbox.neo4j.relationships.retrieve.rule;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.neo4j.graphdb.Relationship;

import java.util.HashMap;
import java.util.List;

import static com.google.common.collect.FluentIterable.from;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.ashlarbox.neo4j.constants.OptionConstants.MOVE_LIMIT;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class RelationshipsLimitSizeRule_UT {

    private final RelationshipsLimitSizeRule relationshipsLimitSizeRule = new RelationshipsLimitSizeRule();

    private static final int MAX_SIZE = 25;
    private static final int LIMIT_SIZE = nextInt(1, MAX_SIZE - 5);

    private HashMap<String, Object> options;
    private List<Relationship> relationships;

    @Before
    public void initializeOptions() {
        options = newHashMap();
    }

    @Before
    public void initializeRelationships() {
        relationships = newArrayList();
        for (int i=0; i < MAX_SIZE; i++) {
            relationships.add(mock(Relationship.class));
        }
    }

    @Test
    public void missingLimitOptionShouldNotApplyFilter() {
        List<Relationship> returnedRelationships = relationshipsLimitSizeRule.apply(from(relationships), options).toList();
        assertThat(returnedRelationships.size(), is(relationships.size()));
        assertThat(returnedRelationships.containsAll(relationships), is(true));
    }

    @Test
    public void limitOptionShouldApplyFilter() {
        options.put(MOVE_LIMIT, LIMIT_SIZE);
        List<Relationship> returnedRelationships = relationshipsLimitSizeRule.apply(from(relationships), options).toList();
        assertThat(returnedRelationships.size(), is(LIMIT_SIZE));
        for (Relationship relationship : returnedRelationships) {
            assertThat(relationships, hasItem(relationship));
        }
    }

}
