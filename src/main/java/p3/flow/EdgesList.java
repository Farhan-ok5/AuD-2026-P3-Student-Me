package p3.flow;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * Compact adjacency-and-edge-list storage used by NetworkFlow.
 *
 * Edges are stored as forward/reverse pairs in a flat list; adjacency lists contain indices into that list.
 * Indices and adjacency structures are zero-based.
 */
public class EdgesList<N> {
    
    /**
     * Edge record holding endpoints and mutable capacity. Used for both forward and reverse edges.
     *
     * The forward edge has the intended capacity; the corresponding reverse edge is created with capacity 0.
     */
    public class Edge {
        private final N from;
        private final N to;
        private int capacity;
        
        /**
         * Construct an edge record.
         *
         * @param from source node identifier
         * @param to target node identifier
         * @param capacity initial capacity of this edge (non-negative normally)
         */
        public Edge(N from, N to, int capacity) {
            this.from = from;
            this.to = to;
            this.capacity = capacity;
        }

        /**
         * @return the source vertex identifier for this edge
         */
        public N from() {
            return from;
        }

        /**
         * @return the target vertex identifier for this edge
         */
        public N to() {
            return to;
        }

        /**
         * @return current remaining capacity of the edge
         */
        public int capacity() {
            return capacity;
        }

        /**
         * Update the capacity stored in this edge record.
         *
         * @param capacity new capacity value (may be used to subtract or add flow)
         */
        public void setCapacity(int capacity) {
            this.capacity = capacity;
        }
    }

    private final List<Edge> edges;
    private final Map<N, List<Integer>> adjacencyList;

    public EdgesList() {
        this.edges = new ArrayList<>();
        this.adjacencyList = new HashMap<>();
    }

    /**
     * Clear the edge list and adjacency list.
     */
    public void clear() {
        edges.clear();
        adjacencyList.clear();
    }

    /**
     * Append a directed edge (from->to) with the given capacity and an implicit reverse edge with 0 capacity.
     * The adjacency list stores edge indices into the flat edges list.
     *
     * @param from source vertex identifier (non-null)
     * @param to target vertex identifier (non-null)
     * @param capacity capacity of the forward edge
     */
    public void addEdge(N from, N to, int capacity) {
        Edge edge = new Edge(from, to, capacity);
        edges.add(edge);
        adjacencyList.computeIfAbsent(from, k -> new ArrayList<>()).add(edges.size() - 1);
        Edge edge2 = new Edge(to, from, 0);
        edges.add(edge2);
        adjacencyList.computeIfAbsent(to, k -> new ArrayList<>()).add(edges.size() - 1);
    }

    /**
     * Return the internal flat list of edges (forward and reverse pairs).
     *
     * @return a immutable List containing Edge records; indices into this list are used by flow algorithms
     */
    public List<Edge> getEdges() {
        return List.copyOf(edges);
    }

    /**
     * Return the Edge at the given index.
     *
     * @param edgeIndex zero-based index into the flat edge list
     * @return the Edge at edgeIndex
     * @throws IndexOutOfBoundsException if edgeIndex is invalid
     */
    public Edge getEdge(int edgeIndex) {
        return edges.get(edgeIndex);
    }

    /**
     * Return the paired reverse edge for a forward edge index.
     *
     * @param edgeIndex index of a forward or backward edge
     * @return the paired reverse Edge record
     * @throws IndexOutOfBoundsException if edgeIndex is out of range
     */
    public Edge getReverseEdge(int edgeIndex) {
        return edges.get(edgeIndex ^ 1);
    }

    /**
     * Return indices of edges that originate from the given node.
     *
     * @param node vertex identifier to query
     * @return a immutable list of edge indices that have {@code node} as their source; empty list when none exist
     */
    public List<Integer> getAdjacentEdges(N node) {
        return List.copyOf(adjacencyList.getOrDefault(node, List.of()));
    }
}
