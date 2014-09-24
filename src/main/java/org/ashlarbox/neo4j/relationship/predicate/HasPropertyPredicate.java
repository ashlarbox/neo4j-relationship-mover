package org.ashlarbox.neo4j.relationship.predicate;

import com.google.common.base.Predicate;
import org.neo4j.graphdb.Relationship;

public class HasPropertyPredicate {

    public static Predicate<Relationship> hasProperty(final String key, final Object value) {
        return new Predicate<Relationship>() {

            @Override
            public boolean apply(Relationship relationship) {
                return relationship.hasProperty(key) && value.equals(relationship.getProperty(key));
            }
        };
    }
}
