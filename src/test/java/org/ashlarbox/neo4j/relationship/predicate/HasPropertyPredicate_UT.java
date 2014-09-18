package org.ashlarbox.neo4j.relationship.predicate;

import com.google.common.base.Predicate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.neo4j.graphdb.Relationship;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.ashlarbox.neo4j.relationship.predicate.HasPropertyPredicate.hasProperty;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HasPropertyPredicate_UT {

    @Mock private Relationship relationship;
    private final String key = randomAlphabetic(8);
    private final Predicate<Relationship> predicate = hasProperty(key);

    @Test
    public void predicateShouldReturnTrueWhenRelationshipHasKey() {
        when(relationship.hasProperty(key)).thenReturn(true);
        assertThat(predicate.apply(relationship), is(true));
    }

    @Test
    public void predicateShouldReturnFalseWhenRelationshipDoesNotHaveKey() {
        when(relationship.hasProperty(key)).thenReturn(false);
        assertThat(predicate.apply(relationship), is(false));
    }

}
