package com.faust.ticketing.core.graph;

import javax.persistence.EntityGraph;
import java.util.HashMap;
import java.util.Map;

public class GraphHelper {
    public static final String FETCHGRAPH = "javax.persistence.fetchgraph";

    public static Map<String, Object> getHints(final EntityGraph entityGraph) {
        final Map<String, Object> hints = new HashMap<>();
        hints.put(FETCHGRAPH, entityGraph);
        return hints;
    }
}
