package p3.minimumSpanningTree;

import org.jetbrains.annotations.NotNull;
import org.tudalgo.algoutils.student.annotation.DoNotTouch;
import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;
import p3.graph.Edge;
import p3.graph.Graph;
import p3.graph.WeightedAdjacencyMatrix;

import java.util.*;
import java.util.stream.Collectors;

public class KruskalSolver {

    /**
     * The class {@link KruskalSolver} should not be created.
     */
    @DoNotTouch
    private KruskalSolver() {
        // do nothing
    }


    /**
     * Returns a {@link List} of edges (and nodes) that are in the graph and sorted according to weight.
     *
     * @param edges The specified set of edges
     * @return The list of edges (and nodes) that are in the graph and sorted according to weight.
     *
     * @param <N> The node type
     */
    @StudentImplementationRequired
    public static <N> List<Edge<N>> sorted(final @NotNull Set<Edge<N>> edges) {
        //TODO: H3.1.1
        return edges.stream().sorted().collect(Collectors.toCollection(ArrayList::new));
    }


    /**
     * This static method performs a set union of two specified {@link Set sets}.
     * It eliminates all duplicates.
     *
     * @param a The first set
     * @param b The second set
     * @return The union of both sets.
     * @param <N> The generic parameter of the set.
     */
    @StudentImplementationRequired
    public static <N> Set<N> union(final @NotNull Set<N> a, final @NotNull Set<N> b) {
        //TODO: H3.1.2
        Set<N> result = new HashSet<>(a);
        result.addAll(b);
        return result;
    }


    @StudentImplementationRequired
    public static <N> Set<Edge<N>> solve(final @NotNull Graph<N> graph) {
        //TODO: H3.2.1
        Set<Edge<N>> result = new HashSet<>();
        List<Edge<N>> edges = graph.getEdges().stream().sorted().collect(Collectors.toCollection(ArrayList::new));
        Map<N, Set<N>> nodesSets = new HashMap<>();

        // jeder knoten wird mit einer menge init in der es selbst drin ist
        for (N node : graph.getNodes()) {
            Set<N> set = new HashSet<>();
            set.add(node);
            nodesSets.put(node, set);
        }

        // wenn knoten ungleiche mengen haben dann aufnehmen
        for (Edge<N> edge : edges) {
            Set<N> setFrom = nodesSets.get(edge.from());
            Set<N> setTo = nodesSets.get(edge.to());

            if (!setFrom.equals(setTo)) {
                result.add(edge);
                Set<N> union = union(setFrom, setTo);
                nodesSets.put(edge.from(), union);
                nodesSets.put(edge.to(), union);
            }
        }
        return result;
    }


    @StudentImplementationRequired
    public static WeightedAdjacencyMatrix getResultAsAdjacencyMatrix(final @NotNull Graph<Integer> graph) {
        //TODO: H3.2.2
        Set<Edge<Integer>> edges = graph.getEdges();
        WeightedAdjacencyMatrix matrix = new WeightedAdjacencyMatrix(graph.getNodes().size());
        for (Edge<Integer> edge : edges) {
            matrix.addEdge(edge.from(), edge.to(), edge.weight());
        }
        return matrix;
    }

} // end of class
