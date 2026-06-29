package p3.dijkstra;

import p3.graph.Edge;
import p3.graph.Graph;

import java.util.*;

import static org.tudalgo.algoutils.student.Student.crash;

/**
 * Computes single-source shortest paths in a weighted, directed {@link Graph} using Dijkstra's algorithm
 * (Foliensatz 6, Folie 151).
 *
 * <p>Dijkstra's algorithm assumes that every edge in the graph has a non-negative weight. Under this assumption,
 * the algorithm extracts the not-yet-visited node {@code u} with the smallest tentative distance from a set {@code Q}
 * and relaxes every outgoing edge of {@code u} until {@code Q} is empty.</p>
 *
 * <p>Intermediate results of the relaxation procedure are stored in two maps:</p>
 * <ul>
 *   <li>{@link #distances} – the currently known shortest distance from the start node to each node;</li>
 *   <li>{@link #predecessors} – the predecessor of each node on the corresponding shortest path.</li>
 * </ul>
 *
 * <p>Unknown / infinite distances are represented by {@link Integer#MAX_VALUE} as required by the exercise.</p>
 *
 * @param <N> the type of the nodes in the graph this calculator operates on
 */
public class DijkstraPathCalculator<N> {

    /**
     * The graph this calculator operates on. The graph is not modified by the calculator.
     */
    protected final Graph<N> graph;

    /**
     * The currently known shortest distance from the start node to each node.
     * Nodes that have not yet been reached map to {@link Integer#MAX_VALUE}.
     */
    protected final Map<N, Integer> distances = new HashMap<>();

    /**
     * The predecessor of each node on the currently known shortest path from the start node.
     * Nodes that have no known predecessor map to {@code null}.
     */
    protected final Map<N, N> predecessors = new HashMap<>();

    /**
     * Construct a new {@link DijkstraPathCalculator} for the given graph.
     *
     * @param graph the graph the calculator will operate on (must be non-null)
     */
    public DijkstraPathCalculator(Graph<N> graph) {
        this.graph = graph;
    }

    /**
     * Initialise the single-source shortest path state for the given start node.
     *
     * <p>Behaves identically to {@code initSSSP(G, s, w)} in the lecture pseudocode: every node's distance is set to
     * infinity (represented by {@link Integer#MAX_VALUE}) and every predecessor is set to {@code null}. The distance
     * of the start node is then set to {@code 0}.</p>
     *
     * @param start the start node {@code s} of the shortest path search
     */
    public void initSSSP(N start) {
        // H2.1 - TODO
        crash("Not implemented yet");
    }

    /**
     * Relax the given edge {@code (u, v)} according to the lecture pseudocode (Foliensatz 6, Folie 129).
     *
     * <p>If the distance to {@code v} can be reduced by going through {@code u} via this edge, the entry for {@code v}
     * in {@link #distances} is updated and {@code u} is recorded as {@code v}'s predecessor.</p>
     *
     * <p>Edges leaving a node with distance {@link Integer#MAX_VALUE} are skipped to avoid an integer overflow when
     * computing {@code u.dist + w((u,v))}.</p>
     *
     * @param edge the edge {@code (u, v)} to relax
     */
    public void relax(Edge<N> edge) {
        // H2.2 - TODO
        crash("Not implemented yet");
    }

    /**
     * Find the node in {@code Q} with the currently smallest distance, remove it from {@code Q} and return it.
     *
     * <p>Corresponds to {@code EXTRACT-MIN(Q)} in the lecture pseudocode. The set {@code Q} contains the nodes that
     * have not yet been visited. A linear scan over {@code Q} is sufficient – using a priority queue would change the
     * asymptotic running time but is not required by the exercise.</p>
     *
     * @param Q the set of not-yet-visited nodes; this set is mutated by removing the returned node
     * @return the node in {@code Q} with the smallest entry in {@link #distances}, or {@code null} if {@code Q} is empty
     */
    public N extractMin(Set<N> Q) {
        // H2.3 - TODO
        crash("Not implemented yet");
        return null;
    }

    /**
     * Process the graph as described in lines 2-6 of the {@code Dijkstra-SSSP} pseudocode.
     *
     * <p>The set {@code Q} of not-yet-visited nodes is initialised with all nodes of the graph. While {@code Q} is
     * not empty, the node {@code u} with the smallest tentative distance is extracted via {@link #extractMin(Set)}
     * and every outgoing edge {@code (u, v)} is relaxed.</p>
     */
    public void processGraph() {
        // H2.4 - TODO
        crash("Not implemented yet");
    }

    /**
     * Compute the shortest path from {@code start} to {@code end} using Dijkstra's algorithm.
     *
     * <p>This is the entry-point method that orchestrates {@link #initSSSP(Object)} and {@link #processGraph()}
     * according to the lecture pseudocode and finally reconstructs the path via {@link #reconstructPath(Object, Object)}.</p>
     *
     * <p>The graph is expected to contain only non-negative edge weights. Calling this method on a graph with
     * negative weights may yield incorrect results.</p>
     *
     * @param start the start node of the path
     * @param end   the end node of the path
     * @return the list of nodes on the shortest path from {@code start} to {@code end}, or an empty list if
     *         {@code end} is not reachable from {@code start}
     */
    public List<N> calculatePath(N start, N end) {
        // H2.5 - TODO
        crash("Not implemented yet");
        return null;
    }

    /**
     * Reconstruct the path from {@code start} to {@code end} by following {@link #predecessors} backwards from
     * {@code end}.
     *
     * <p>The method assumes that {@link #predecessors} has been filled by a previous call to {@link #processGraph()}
     * (or equivalent). If {@code end} is not reachable from {@code start}, an empty list is returned.</p>
     *
     * @param start the start node of the path
     * @param end   the end node of the path
     * @return the list of nodes on the path from {@code start} to {@code end}, or an empty list if no such path exists
     */
    protected List<N> reconstructPath(N start, N end) {
        List<N> path = new ArrayList<>();
        N current = end;
        while (current != null && !current.equals(start)) {
            path.add(current);
            current = predecessors.get(current);
        }

        if (current == null) {
            return Collections.emptyList();
        }

        path.add(start);
        Collections.reverse(path);
        return path;
    }
}
