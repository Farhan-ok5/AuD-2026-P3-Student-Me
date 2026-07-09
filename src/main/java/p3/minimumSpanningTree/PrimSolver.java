package p3.minimumSpanningTree;

import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import org.tudalgo.algoutils.student.annotation.DoNotTouch;
import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;
import p3.graph.Edge;
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
        //TODO: H3.1.3
        return map.keySet().stream().min(Comparator.comparing(map::get)).get();
    }


    @StudentImplementationRequired
    public static WeightedAdjacencyMatrix solve(final @NonNull Graph<Integer> graph, final Integer root) {
        //TODO: H3.3
        WeightedAdjacencyMatrix matrix = new WeightedAdjacencyMatrix(graph.getNodes().size());

        Set<Integer> immutableCopy = graph.getNodes();
        Set<Integer> q = new HashSet<>(immutableCopy);

        Map<Integer, Integer> keys = new HashMap<>();
        Map<Integer, Integer> preds = new HashMap<>();

        // init
        for (Integer node : q) {
            keys.put(node, INFINITE);
            preds.put(node, null);
        }
        keys.put(root, MINUS_INFINITE);


        while (!q.isEmpty()) {
            // extract-min
            Integer u = q.stream()
                .min(Comparator.comparing(keys::get))
                .get();
            q.remove(u);

            Set<Edge<Integer>> adjEdges =  graph.getAllAdjacentEdges(u);
            if (adjEdges != null) {
                for (Edge<Integer> edge : adjEdges) {
                    Integer v = edge.to();
                    int weight = edge.weight();

                    // falls pfeil auf u zeigt
                    if (v.equals(u)) {
                        v = edge.from();
                    }

                    if (q.contains(v) && weight < keys.get(v)) {
                        keys.put(v, weight);
                        preds.put(v, u);
                    }
                }
            }
        }

        for (Integer v : immutableCopy) {
            Integer u = preds.get(v);
            if (u != null) {
                matrix.addEdge(u, v, keys.get(v));
            }
        }
        return matrix;
    }

} // end of class
