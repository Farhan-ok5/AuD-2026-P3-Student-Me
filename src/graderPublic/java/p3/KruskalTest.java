package p3;

import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSet;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSetTest;
import p3.graph.Edge;
import p3.graph.PublicEdgeFactory;
import p3.minimumSpanningTree.KruskalSolver;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static util.AssertionUtil.assertEquals;

@TestForSubmission
public class KruskalTest extends P3_TestBase {

    @Override
    public String getTestedClassName() {
        return "KruskalSolver";
    }

    @Override
    public List<String> getOptionalParams() {
        return List.of("from", "to", "edgeList", "expected");
    }


    /**
     * This test method is used to test the method {@code sorted} from task H3, Aufgabe 1 (a).
     *
     * @param params The parameters for the test.
     */
    @ParameterizedTest
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    @JsonParameterSetTest(value = "minimumSpanningTree/sorted.json")
    public void testSorted(final @NonNull JsonParameterSet params) {

        final Context.Builder<?> context = createContext(params, "sorted");

        final Set<Edge<Integer>> getEdgesFromTestGraph = makeEdgesFromList(params);
        final List<List<Integer>> expected = params.get("sortedEdges");

        final List<Edge<Integer>> actualTmp = callObject(
                () -> KruskalSolver.sorted(getEdgesFromTestGraph),
                context,
                "sorted"
        );

        final List<List<Integer>> actual = convertEdgeListToListOfList(actualTmp);

        assertEquals(expected, actual, context, "The 'sorted' method returns an unexpected result.");
    }


    /**
     * This test method is used to test the method {@code union} from task H3, Aufgabe 1 (b).
     *
     * @param params The parameters for the test.
     */
    @ParameterizedTest
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    @JsonParameterSetTest(value = "minimumSpanningTree/testUnion.json")
    public void testUnion(final @NonNull JsonParameterSet params) {

        final Context.Builder<?> context = createContext(params, "union");

        final List<Integer> from1 = params.get("from1");
        final List<Integer> to1 = params.get("to1");
        final List<Integer> weight1 = params.get("weight1");

        final List<Integer> from2 = params.get("from2");
        final List<Integer> to2 = params.get("to2");
        final List<Integer> weight2 = params.get("weight2");

        final List<List<Integer>> expectedTmp = params.get("expected");
        final Set<List<Integer>> expected = new HashSet<>(expectedTmp);

        final Set<Edge<Integer>> set1 = new HashSet<>();
        final Set<Edge<Integer>> set2 = new HashSet<>();

        for(int i = 0 ; i < from1.size(); i++) {
            set1.add( PublicEdgeFactory.createEdge( from1.get(i), to1.get(i), weight1.get(i)) );
        }

        for(int i = 0; i < from2.size(); i++) {
            set2.add( PublicEdgeFactory.createEdge( from2.get(i), to2.get(i), weight2.get(i) ) );
        }

        final Set<Edge<Integer>> union = callObject(
                () -> KruskalSolver.union(set1, set2),
                context,
                "union"
        );

        final Set<List<Integer>> actual = new HashSet<>();

        for(Edge<Integer> edge : union) {
            actual.add( List.of( edge.from(), edge.to(), edge.weight() ) );
        }

        assertEquals(expected, actual, context, "The 'union' method returns an unexpected result.");
    }


    @JsonParameterSetTest(value = "minimumSpanningTree/sorted.json")
    private @NonNull Set<Edge<Integer>> makeEdgesFromList(final @NonNull JsonParameterSet params) {

        final List<Integer> listOfFrom = params.get("from");
        final List<Integer> listOfTo = params.get("to");
        final List<Integer> listOfWeights = params.get("weight");

        if ( ! ( listOfFrom.size() == listOfTo.size() && listOfTo.size() == listOfWeights.size() ) ) {
            throw new RuntimeException("Cannot run test. The three lists must have equal length.");
        } // end of if

        final Set<Edge<Integer>> result = new HashSet<>();

        for(int i = 0 ; i < listOfFrom.size(); i++) {
            result.add( PublicEdgeFactory.createEdge( listOfFrom.get(i), listOfTo.get(i), listOfWeights.get(i) ) );
        } // end of for

        return result;
    }


    private @NonNull List<List<Integer>> convertEdgeListToListOfList(final @NonNull List<Edge<Integer>> edges ) {
        List<List<Integer>> result = new ArrayList<>();

        for(final Edge<Integer> edge : edges) {
            result.add( List.of( edge.from(), edge.to(), edge.weight() ) );
        } // end of for-each

        return result;
    }

}
