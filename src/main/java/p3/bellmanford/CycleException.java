package p3.bellmanford;

/**
 * Thrown by {@link BellmanFordPathCalculator#calculatePath(Object, Object)} when the input graph contains a
 * negative-weight cycle reachable from the start node.
 *
 * <p>In that case no shortest path is well-defined because distances along the cycle can be reduced indefinitely.</p>
 */
public class CycleException extends RuntimeException {

    /**
     * Construct a {@link CycleException} with a default message.
     */
    public CycleException() {
        super("Negative cycle detected in the graph");
    }

    /**
     * Construct a {@link CycleException} with a custom message.
     *
     * @param message the detail message
     */
    public CycleException(String message) {
        super(message);
    }
}
