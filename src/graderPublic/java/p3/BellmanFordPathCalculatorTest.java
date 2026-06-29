package p3;

import org.junit.jupiter.params.ParameterizedTest;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSet;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSetTest;
import p3.bellmanford.BellmanFordPathCalculator;
import p3.bellmanford.CycleException;
import p3.graph.Edge;
import p3.graph.Graph;
import p3.implementation.TestGraph;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static util.AssertionUtil.*;

@TestForSubmission
public class BellmanFordPathCalculatorTest extends P3_TestBase {

    @Override
    public String getTestedClassName() {
        return "BellmanFordPathCalculator";
    }

    @Override
    public List<String> getOptionalParams() {
        return List.of(
            "nodes", "edges", "start", "end",
            "initialDistances", "initialPredecessors",
            "expectedDistances", "expectedPredecessors", "expectedPath",
            "expected", "relaxFrom", "relaxTo"
        );
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "bellmanford/initSSSP.json")
    public void testInitSSSP(JsonParameterSet params) throws ReflectiveOperationException {
        Context.Builder<?> context = createContext(params, "initSSSP");
        BellmanFordPathCalculator<Integer> calc = createCalculator(params);

        int start = params.get("start");
        call(() -> calc.initSSSP(start), context, "initSSSP");

        Map<Integer, Integer> expectedDistances = createDistanceMap(params, "expectedDistances");
        Map<Integer, Integer> expectedPredecessors = createPredecessorMap(params, "expectedPredecessors");

        assertMapEquals(expectedDistances, getDistances(calc), context, "distances");
        assertMapEquals(expectedPredecessors, getPredecessors(calc), context, "predecessors");
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "bellmanford/relax.json")
    public void testRelax(JsonParameterSet params) throws ReflectiveOperationException {
        Context.Builder<?> context = createContext(params, "relax");
        Graph<Integer> graph = createGraph(params);
        BellmanFordPathCalculator<Integer> calc = new BellmanFordPathCalculator<>(graph);

        setDistances(calc, createDistanceMap(params, "initialDistances"));
        setPredecessors(calc, createPredecessorMap(params, "initialPredecessors"));

        int from = params.get("relaxFrom");
        int to = params.get("relaxTo");
        Edge<Integer> edge = graph.getEdge(from, to);

        context.add("edge to relax", edge);

        call(() -> calc.relax(edge), context, "relax");

        Map<Integer, Integer> expectedDistances = createDistanceMap(params, "expectedDistances");
        Map<Integer, Integer> expectedPredecessors = createPredecessorMap(params, "expectedPredecessors");

        assertMapEquals(expectedDistances, getDistances(calc), context, "distances");
        assertMapEquals(expectedPredecessors, getPredecessors(calc), context, "predecessors");
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "bellmanford/hasNegativeCycle.json")
    public void testHasNegativeCycle(JsonParameterSet params) throws ReflectiveOperationException {
        Context.Builder<?> context = createContext(params, "hasNegativeCycle");
        BellmanFordPathCalculator<Integer> calc = createCalculator(params);

        setDistances(calc, createDistanceMap(params, "distances"));
        setPredecessors(calc, createPredecessorMap(params, "predecessors"));

        boolean expected = params.get("expected");
        boolean actual = callObject(calc::hasNegativeCycle, context, "hasNegativeCycle");

        context.add("actual", actual);

        assertEquals(expected, actual, context,
            "hasNegativeCycle returned the wrong result for the given graph.");
    }

    private BellmanFordPathCalculator<Integer> createCalculator(JsonParameterSet params) {
        return new BellmanFordPathCalculator<>(createGraph(params));
    }

    private Graph<Integer> createGraph(JsonParameterSet params) {
        List<Integer> nodes = params.get("nodes");
        Set<Edge<Integer>> edges = getEdges(params);
        return new TestGraph<>(new HashSet<>(nodes), edges);
    }

    @SuppressWarnings("unchecked")
    private static Map<Integer, Integer> getDistances(BellmanFordPathCalculator<Integer> calc)
            throws ReflectiveOperationException {
        Field field = BellmanFordPathCalculator.class.getDeclaredField("distances");
        field.setAccessible(true);
        return (Map<Integer, Integer>) field.get(calc);
    }

    @SuppressWarnings("unchecked")
    private static Map<Integer, Integer> getPredecessors(BellmanFordPathCalculator<Integer> calc)
            throws ReflectiveOperationException {
        Field field = BellmanFordPathCalculator.class.getDeclaredField("predecessors");
        field.setAccessible(true);
        return (Map<Integer, Integer>) field.get(calc);
    }

    private static void setDistances(BellmanFordPathCalculator<Integer> calc, Map<Integer, Integer> distances)
            throws ReflectiveOperationException {
        Map<Integer, Integer> map = getDistances(calc);
        map.clear();
        map.putAll(distances);
    }

    private static void setPredecessors(BellmanFordPathCalculator<Integer> calc, Map<Integer, Integer> predecessors)
            throws ReflectiveOperationException {
        Map<Integer, Integer> map = getPredecessors(calc);
        map.clear();
        map.putAll(predecessors);
    }
}
