package p3;

import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSet;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSetTest;
import p3.graph.BasicGraph;
import p3.graph.Edge;
import p3.graph.Graph;
import p3.graph.PublicEdgeFactory;
import p3.minimumSpanningTree.PrimSolver;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static util.AssertionUtil.assertEquals;

@TestForSubmission
public class PrimTest extends P3_TestBase {


    @Override
    public String getTestedClassName() {
        return "PrimSolver";
    }

    @Override
    public List<String> getOptionalParams() {
        return List.of();
    }

    /**
     * This test method is used to test the method {@code extractMin} from task H3, Aufgabe 1 (c).
     *
     * @param params The parameters for the test.
     */
    @ParameterizedTest
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    @JsonParameterSetTest(value = "minimumSpanningTree/testExtractMin.json")
    public void testExtractMin(final @NonNull JsonParameterSet params) {

        final Context.Builder<?> context = createContext(params, "extractMin");

        final Map<String, Integer> mapTmp = params.get("map");

        // Conversion needed since JSON only store keys as String instead of Integer.
        final Map<Integer, Integer> map = new HashMap<>();

        // Puts the element from mapTmp into map
        for(Map.Entry<String, Integer> entry : mapTmp.entrySet()) {
            map.put( Integer.parseInt(entry.getKey()), entry.getValue() );
        }

        final int expected = params.get("expected");
        final int actual = callObject(
                () -> PrimSolver.extractMin(map),
                context,
                "extractMin"
        );

        assertEquals(expected, actual, context, "The 'extractMin' method returns an unexpected result.");

    }

    @ParameterizedTest
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    @JsonParameterSetTest(value = "minimumSpanningTree/primSolverData.json")
    public void testSolve(final @NonNull JsonParameterSet params) {

        final Context.Builder<?> context = createContext(params, "solve");

        final List<Integer> nodesOfTestGraphAsList = params.get("nodes");
        final Set<Integer> nodesOfTestGraph = new HashSet<>(nodesOfTestGraphAsList);

        final List<Integer> list1 = params.get("from");
        final List<Integer> list2 = params.get("to");
        final List<Integer> list3 = params.get("weight");

        final Set<Edge<Integer>> edgesOfTestGraphAsEdge = new HashSet<>();

        for(int i = 0 ; i < list1.size(); i++) {
            edgesOfTestGraphAsEdge.add( PublicEdgeFactory.createEdge(list1.get(i), list2.get(i), list3.get(i)) );
        } // end of for-each

        final Graph<Integer> graph = new BasicGraph<>(nodesOfTestGraph, edgesOfTestGraphAsEdge);
        final int root = params.get("root");

        final int[][] actualTmp = callObject(
                () -> PrimSolver.solve(graph, root),
                context,
                "solve"
        ).getMatrix();

        final List<List<Integer>> expected = params.get("expectedMatrixMST");

        final List<List<Integer>> actual = new ArrayList<>();

        for(int[] row : actualTmp) {
            final List<Integer> sublist = new ArrayList<>();

            for(int entry : row) {
                sublist.add(entry);
            }
            actual.add(sublist);
        }

        assertEquals(expected, actual, context, "solve");

    }
}
