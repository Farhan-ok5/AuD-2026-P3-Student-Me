package p3.bellmanford;

import p3.graph.Edge;
import p3.graph.Graph;

import java.util.*;

import static org.tudalgo.algoutils.student.Student.crash;

/**
 * Computes single-source shortest paths in a weighted, directed {@link Graph} using the Bellman-Ford algorithm
 * (Foliensatz 6, Folie 130).
 *
 * <p>In contrast to Dijkstra's algorithm, Bellman-Ford supports edges with negative weights and is able to detect
 * negative cycles reachable from the start node. When such a cycle is present, {@link #calculatePath(Object, Object)}
 * raises a {@link CycleException} because no shortest path is well-defined.</p>
 *
 * <p>The calculator stores the intermediate result of the relaxation procedure in two maps:</p>
 * <ul>
 *   <li>{@link #distances} – the currently known shortest distance from the start node to each node;</li>
 *   <li>{@link #predecessors} – the predecessor of each node on the corresponding shortest path.</li>
 * </ul>
 *
 * <p>Unknown / infinite distances are represented by {@link Integer#MAX_VALUE} as required by the exercise.</p>
 *
 * @param <N> the type of the nodes in the graph this calculator operates on
 */
public class BellmanFordPathCalculator<N> {

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
     * Construct a new {@link BellmanFordPathCalculator} for the given graph.
     *
     * @param graph the graph the calculator will operate on (must be non-null)
     */
    public BellmanFordPathCalculator(Graph<N> graph) {
        this.graph = graph;
    }

    /**
     * Initialise the single-source shortest path state for the given start node.
     *
     * <p>Corresponds to {@code initSSSP(G, s, w)} in the lecture pseudocode: every node's distance is set to infinity
     * (represented by {@link Integer#MAX_VALUE}) and every predecessor is set to {@code null}. The distance of the
     * start node is then set to {@code 0}.</p>
     *
     * @param start the start node {@code s} of the shortest path search
     */
    public void initSSSP(N start) {
        //TODO: H1.1
        for (N node : graph.getNodes()) {
            distances.put(node, Integer.MAX_VALUE);
            predecessors.put(node, null);
        }
        distances.put(start, 0);
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
        //TODO: H1.2
        N u = edge.from();
        N v = edge.to();
        if (distances.get(u) == Integer.MAX_VALUE) {return;}
        int calcDist = distances.get(u) + edge.weight();
        if (distances.get(v) > (calcDist)) {
            distances.replace(v, calcDist);
            predecessors.replace(v, u);
        }
    }

    /**
     * Relax every edge of the graph {@code |V| - 1} times.
     *
     * <p>Corresponds to lines 2-4 of the {@code Bellman-Ford-SSSP} pseudocode. After this method has finished, the
     * distance map contains the final shortest distances if and only if the graph does not contain a negative cycle
     * reachable from the start node.</p>
     */
    public void processGraph() {
        //TODO H1.3
        Set<Edge<N>> edges =  graph.getEdges();
        for (int i = 0; i < edges.size(); i++) {
            for (Edge<N> edge : edges) {
                relax(edge);
            }
        }
    }

    /**
     * Check whether the graph contains a negative cycle reachable from the start node.
     *
     * <p>Corresponds to lines 5-7 of the {@code Bellman-Ford-SSSP} pseudocode. The method iterates over every edge
     * {@code (u, v)} once more after {@link #processGraph()}: if any edge can still be relaxed (i.e. the condition
     * {@code v.dist > u.dist + w((u, v))} holds for an edge whose source has a finite distance), a negative cycle
     * exists and the method returns {@code true}.</p>
     *
     * <p>Note: the return value is inverted relative to the pseudocode in the slides. The pseudocode returns
     * {@code false} when a violating edge is found; this method returns {@code true} in that case.</p>
     *
     * @return {@code true} if a negative cycle was detected, {@code false} otherwise
     */
    public boolean hasNegativeCycle() {
        //TODO: H1.4
        Set<Edge<N>> edges =  graph.getEdges();
        for (Edge<N> edge : edges) {
            if (distances.get(edge.to()) > (distances.get(edge.from()) + edge.weight())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Compute the shortest path from {@code start} to {@code end} using Bellman-Ford.
     *
     * <p>This is the entry-point method that orchestrates {@link #initSSSP(Object)}, {@link #processGraph()} and
     * {@link #hasNegativeCycle()} according to the lecture pseudocode. If a negative cycle is detected, a
     * {@link CycleException} is thrown because no shortest path is well-defined.</p>
     *
     * @param start the start node of the path
     * @param end   the end node of the path
     * @return the list of nodes on the shortest path from {@code start} to {@code end}, or an empty list if
     *         {@code end} is not reachable from {@code start}
     * @throws CycleException if the graph contains a negative cycle reachable from {@code start}
     */
    public List<N> calculatePath(N start, N end) {
        // H1.5 - TODO
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
