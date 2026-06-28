package p3;

import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSet;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSetTest;
import p3.minimumSpanningTree.PrimSolver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static util.AssertionUtil.assertEquals;

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
}
