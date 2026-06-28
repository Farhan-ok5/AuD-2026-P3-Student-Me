package p3.flow;

import java.util.Map;
import java.util.HashMap;
import java.util.Queue;
import java.util.LinkedList;

/**
 * Simple Edmonds–Karp style max-flow wrapper using an EdgesList storage.
 *
 * Type parameter {@code N} is the vertex identifier type used by callers (e.g., String).
 */
public class NetworkFlow<N> {
    
    private final EdgesList<N> edgesList;

    public NetworkFlow() {
        this.edgesList = new EdgesList<>();
    }

    /**
     * Reset the flow network to its initial state.
     */
    public void reset() {
        this.edgesList.clear();
    }

    /**
     * Add a directed edge with the given capacity to the internal edge list.
     * A reverse edge with zero capacity is added implicitly.
     *
     * @param from source vertex identifier (must be non-null)
     * @param to target vertex identifier (must be non-null)
     * @param capacity non-negative capacity of the forward edge
     * @throws IllegalArgumentException if capacity is negative
     */
    public void addEdge(N from, N to, int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("Capacity must be non-negative");
        }
        edgesList.addEdge(from, to, capacity);
    }

    /**
     * @return the {@link EdgesList} instance backing this flow network (live reference)
     */
    public EdgesList<N> getEdgesList() {
        return edgesList;
    }

    /**
     * Breadth-first search that finds an augmenting path from source to sink.
     *
     * @param source source vertex (must be non-null)
     * @param sink sink vertex (must be non-null)
     * @param parent mutable map that will be filled with (node -> incoming edgeIndex) along the found path
     * @return {@code true} when an augmenting path to sink was found, {@code false} otherwise
     */
    private boolean bfs(N source, N sink, Map<N, Integer> parent) {
        parent.clear();
        if (source.equals(sink)) {
            return true;
        }
        parent.put(source, null);
        Queue<N> queue = new LinkedList<>();
        queue.add(source);
        while (!queue.isEmpty()) {
            N current = queue.poll();
            for (int edgeIndex : edgesList.getAdjacentEdges(current)) {
                EdgesList<N>.Edge edge = edgesList.getEdge(edgeIndex);
                if (edge.capacity() > 0 && !parent.containsKey(edge.to())) {
                    parent.put(edge.to(), edgeIndex);
                    if (edge.to().equals(sink)) {
                        return true;
                    }
                    queue.add(edge.to());
                }
            }
        }        
        return false;
    }

    /**
     * Push the bottleneck flow along the path recorded in {@code parent}, updating capacities
     * on forward and reverse edges.
     *
     * @param source source vertex of the path (non-null)
     * @param sink sink vertex of the path (non-null)
     * @param parent mapping each visited node to the edge index used to reach it
     * @return the amount of flow pushed along the path (positive integer)
     * @throws NullPointerException if parent is missing entries for nodes on the path
     */
    private int pushFlow(N source, N sink, Map<N, Integer> parent) {
        int flow = Integer.MAX_VALUE;
        for (N current = sink; !current.equals(source); ) {
            int edgeIndex = parent.get(current);
            EdgesList<N>.Edge edge = edgesList.getEdge(edgeIndex);
            flow = Math.min(flow, edge.capacity());
            current = edge.from();
        }
        for (N current = sink; !current.equals(source); ) {
            int edgeIndex = parent.get(current);
            EdgesList<N>.Edge edge = edgesList.getEdge(edgeIndex);
            edge.setCapacity(edge.capacity() - flow);
            EdgesList<N>.Edge reverseEdge = edgesList.getReverseEdge(edgeIndex);
            reverseEdge.setCapacity(reverseEdge.capacity() + flow);
            current = edge.from();
        }
        return flow;
    }

    /**
     * Compute maximum flow from source to sink using repeated BFS augmentations (Edmonds–Karp style).
     *
     * @param source source vertex identifier (non-null)
     * @param sink sink vertex identifier (non-null)
     * @return the total maximum flow value from source to sink
     */
    public int maxFlow(N source, N sink) {
        Map<N, Integer> parent = new HashMap<>();
        int totalFlow = 0;
        while (bfs(source, sink, parent)) {
            totalFlow += pushFlow(source, sink, parent);
        }
        return totalFlow;
    }
}
