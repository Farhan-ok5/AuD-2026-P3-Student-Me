package p3.minimumSpanningTree;

import org.jetbrains.annotations.NotNull;
import org.tudalgo.algoutils.student.annotation.DoNotTouch;
import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;
import p3.graph.Edge;
import p3.graph.Graph;
import p3.graph.WeightedAdjacencyMatrix;

import java.util.*;

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
        return null; // TODO: Task H1 (a)
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
        return null; // TODO: Task H1 (b)
    }


    @StudentImplementationRequired
    public static <N> Set<Edge<N>> solve(final @NotNull Graph<N> graph) {
        return null; // TODO: Task H2 (a)
    }


    @StudentImplementationRequired
    public static WeightedAdjacencyMatrix getResultAsAdjacencyMatrix( final @NotNull Graph<Integer> graph ) {
        return null ; // TODO: Task H2 (b)
    }

} // end of class
