package p3.minimumSpanningTree;

import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import org.tudalgo.algoutils.student.annotation.DoNotTouch;
import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;
import p3.graph.Graph;
import p3.graph.WeightedAdjacencyMatrix;

import java.util.*;


public class PrimSolver {

    @DoNotTouch
    private PrimSolver() {
        // do nothing
    }

    /**
     * This attribute represents the number infinite as integer.
     */
    @DoNotTouch
    public static final int INFINITE = Integer.MAX_VALUE;

    /**
     * This attribute represents the number minus infinite as integer.
     */
    @DoNotTouch
    public static final int MINUS_INFINITE = Integer.MIN_VALUE;


    @StudentImplementationRequired
    public static <N> N extractMin(final @NotNull Map<N, Integer> map) {
        return null; // TODO: Task H1 (c)
    }


    @StudentImplementationRequired
    public static WeightedAdjacencyMatrix solve(final @NonNull Graph<Integer> graph, final Integer root) {
        return null; // TODO: Task H3
    }

} // end of class
