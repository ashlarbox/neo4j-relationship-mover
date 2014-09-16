package org.ashlarbox.neo4j.util;

import com.google.common.collect.FluentIterable;

public class FluentIterableUtil {

    public static FluentIterable applyLimit(FluentIterable iterable, Integer moveLimit) {
        return (moveLimit == null) ? iterable : iterable.limit(moveLimit);
    }

}
