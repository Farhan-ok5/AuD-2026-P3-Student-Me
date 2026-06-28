package p3.graph;

import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;
import org.tudalgo.algoutils.student.annotation.DoNotTouch;

@DoNotTouch
public class PublicEdgeFactory {

    @DoNotTouch
    private PublicEdgeFactory() {
        // do nothing
    }

    @Contract("_, _, _ -> new")
    public static <N> @NonNull Edge<N> createEdge(final N from, final N to, final int weight) {
        return new EdgeImpl<>(from, to, weight);
    }

}
