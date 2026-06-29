package p3;

import org.junit.jupiter.params.ParameterizedTest;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSet;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSetTest;
import p3.dijkstra.DijkstraPathCalculator;
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
public class DijkstraPathCalculatorTest extends P3_TestBase {

    @Override
    public String getTestedClassName() {
        return "DijkstraPathCalculator";
    }

    @Override
    public List<String> getOptionalParams() {
        return List.of(
            "nodes", "edges", "start", "end",
            "initialDistances", "initialPredecessors",
            "expectedDistances", "expectedPredecessors", "expectedPath",
            "distances", "Q", "expectedMin", "expectedQ",
            "relaxFrom", "relaxTo"
        );
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "dijkstra/initSSSP.json")
    public void testInitSSSP(JsonParameterSet params) throws ReflectiveOperationException {
        Context.Builder<?> context = createContext(params, "initSSSP");
        DijkstraPathCalculator<Integer> calc = createCalculator(params);

        int start = params.get("start");
        call(() -> calc.initSSSP(start), context, "initSSSP");

        Map<Integer, Integer> expectedDistances = createDistanceMap(params, "expectedDistances");
        Map<Integer, Integer> expectedPredecessors = createPredecessorMap(params, "expectedPredecessors");

        assertMapEquals(expectedDistances, getDistances(calc), context, "distances");
        assertMapEquals(expectedPredecessors, getPredecessors(calc), context, "predecessors");
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "dijkstra/relax.json")
    public void testRelax(JsonParameterSet params) throws ReflectiveOperationException {
        Context.Builder<?> context = createContext(params, "relax");
        Graph<Integer> graph = createGraph(params);
        DijkstraPathCalculator<Integer> calc = new DijkstraPathCalculator<>(graph);

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

    private DijkstraPathCalculator<Integer> createCalculator(JsonParameterSet params) {
        return new DijkstraPathCalculator<>(createGraph(params));
    }

    private Graph<Integer> createGraph(JsonParameterSet params) {
        List<Integer> nodes = params.get("nodes");
        Set<Edge<Integer>> edges = params.availableKeys().contains("edges") ? getEdges(params) : Set.of();
        return new TestGraph<>(new HashSet<>(nodes), edges);
    }

    @SuppressWarnings("unchecked")
    private static Map<Integer, Integer> getDistances(DijkstraPathCalculator<Integer> calc)
            throws ReflectiveOperationException {
        Field field = DijkstraPathCalculator.class.getDeclaredField("distances");
        field.setAccessible(true);
        return (Map<Integer, Integer>) field.get(calc);
    }

    @SuppressWarnings("unchecked")
    private static Map<Integer, Integer> getPredecessors(DijkstraPathCalculator<Integer> calc)
            throws ReflectiveOperationException {
        Field field = DijkstraPathCalculator.class.getDeclaredField("predecessors");
        field.setAccessible(true);
        return (Map<Integer, Integer>) field.get(calc);
    }

    private static void setDistances(DijkstraPathCalculator<Integer> calc, Map<Integer, Integer> distances)
            throws ReflectiveOperationException {
        Map<Integer, Integer> map = getDistances(calc);
        map.clear();
        map.putAll(distances);
    }

    private static void setPredecessors(DijkstraPathCalculator<Integer> calc, Map<Integer, Integer> predecessors)
            throws ReflectiveOperationException {
        Map<Integer, Integer> map = getPredecessors(calc);
        map.clear();
        map.putAll(predecessors);
    }
}
