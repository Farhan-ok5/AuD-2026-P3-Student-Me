package p3;

import org.sourcegrade.jagr.api.rubric.*;
import org.sourcegrade.jagr.api.rubric.Grader.TestAwareBuilder;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSet;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

import static org.sourcegrade.jagr.api.rubric.Grader.TestAwareBuilder;
import static org.sourcegrade.jagr.api.rubric.Grader.testAwareBuilder;

public class P3_RubricProvider implements RubricProvider {

    private static Criterion createUntestedCriterion(String shortDescription, int maxPoints) {
        return Criterion.builder()
                .shortDescription(shortDescription)
                .grader((testCycle, criterion) ->
                        GradeResult.of(criterion.getMinPoints(), criterion.getMaxPoints(), "Not graded by public grader"))
                .maxPoints(maxPoints)
                .build();
    }

    @SafeVarargs
    private static Criterion createCriterion(String shortDescription, int maxPoints, Callable<Method>... methodRefs) {
        TestAwareBuilder graderBuilder = testAwareBuilder();

        for (Callable<Method> methodRef : methodRefs) {
            graderBuilder.requirePass(JUnitTestRef.ofMethod(methodRef));
        }

        return Criterion.builder()
                .shortDescription(shortDescription)
                .grader(graderBuilder.pointsFailedMin().pointsPassedMax().build())
                .maxPoints(maxPoints)
                .build();
    }

    private static Criterion createParentCriterion(String taskId, String shortDescription, Criterion... children) {
        return Criterion.builder()
                .shortDescription("H" + taskId + " | " + shortDescription)
                .addChildCriteria(children)
                .build();
    }


    public static final Criterion H1_1_1 = createCriterion(
            "Die Methode [[[put]]] überschreibt korrekt vorhandene Einträge.",
            1,
            () -> ServerMonitorTest.class.getMethod("testPutOverwriteWithSize", String.class, int.class, int.class)
    );

    public static final Criterion H1_1_2 = createCriterion(
            "Die Methode [[[get]]] liefert korrekte Werte für vorhandene Schlüssel.",
            1,
            () -> ServerMonitorTest.class.getMethod("testGet", String.class, Integer.class, Boolean.class)
    );

    public static final Criterion H1_1_3 = createCriterion(
            "Die Methode [[[hash]]] liefert korrekte Hash-Werte im erwarteten Bereich.",
            1,
            () -> ServerMonitorTest.class.getMethod("testHash", String.class, int.class)
    );

    public static final Criterion H1_1_4 = createCriterion(
            "Die Methode [[[remove]]] entfernt korrekte Elemente aus Hashtabelle.",
            1,
            () -> ServerMonitorTest.class.getMethod("testRemoveWithSize", String.class, int.class, boolean.class)
    );


    public static final Criterion H1_1_5 = createCriterion(
            "Die Methode [[[put]]] speichert Einträge an der korrekten Indexposition und behandelt Kollisionen korrekt.",
            3,
            () -> ServerMonitorTest.class.getMethod("testPutIndexAndChainingWithReflection")
    );

    public static final Criterion H1_1_6 = createCriterion(
            "Die Methode [[[rollingHash]]] liefert korrekte Hash-Werte im erwarteten Bereich [Basic]",
            1,
            () -> ServerMonitorTest.class.getMethod("testRollingHash", String.class, int.class)
    );

    public static final Criterion H1_1_7 = createCriterion(
            "Die Methode [[[rollingHash]]] liefert korrekte Hash-Werte im erwarteten Bereich [PRO]",
            2,
            () -> ServerMonitorTest.class.getMethod("testRollingHashAdv", String.class, int.class)
    );

    public static final Criterion H1_2_1 = createCriterion(
            "The table doubles its size correctly when rehashing.",
            1,
            () -> RehashingTest.class.getMethod("testRehashDoublesTableSize")
    );

    public static final Criterion H1_2_2 = createCriterion(
            "Rehashing method is invoked correctly once current size of table exceeds the given load factor.",
            1,
            () -> RehashingTest.class.getMethod("testRehashMethodIsTriggered")
    );

    public static final Criterion H1_2_3 = createCriterion(
            "All the entries are preserved after successful rehashing.",
            1,
            () -> RehashingTest.class.getMethod("testAllEntriesRemainAfterRehash")
    );

    public static final Criterion H1_3_1 = createCriterion(
            "Closed checks (value = 0) are removed correctly.",
            1,
            () -> CheckManagementTest.class.getMethod("testRemoveClosedCheck")
    );

    public static final Criterion H1_3_2 = createCriterion(
            "New checks are added to the result correctly.",
            1,
            () -> CheckManagementTest.class.getMethod("testAddNewCheck")
    );

    public static final Criterion H1_3_3 = createCriterion(
            "Status and values of existing checks are updated correctly.",
            2,
            () -> CheckManagementTest.class.getMethod("testStatusAndValueUpdateCorrectness")
    );

    public static final Criterion H1_1 = createParentCriterion("1.1", "Hash Tables - CRUD Operations", H1_1_1, H1_1_2, H1_1_3, H1_1_4, H1_1_5, H1_1_6, H1_1_7);
    public static final Criterion H1_2 = createParentCriterion("1.2", "Hash Tables - Dynamic Rehashing", H1_2_1, H1_2_2, H1_2_3);
    public static final Criterion H1_3 = createParentCriterion("1.3", "Check Management for Monitoring", H1_3_1, H1_3_2, H1_3_3);
    public static final Criterion H1 = createParentCriterion("1", "Hash Tables", H1_1, H1_2, H1_3);


    // ====================================================================
    // H2 - GRAPH REPRESENTATIONS (6 Points)
    // ====================================================================

    // H2a) AdjacencyMatrix (2 Points)
    public static final Criterion H2_1_1 = createCriterion(
            "Die Methode [[[addEdge]]] der Klasse [[[AdjacencyMatrix]]] funktioniert vollständig korrekt",
            1,
            () -> AdjacencyMatrixTest.class.getMethod("testAddEdge", JsonParameterSet.class)
    );

    public static final Criterion H2_1_2 = createCriterion(
            "Die Methode [[[getAdjacentIndices]]] der Klasse [[[AdjacencyMatrix]]] funktioniert vollständig korrekt",
            1,
            () -> AdjacencyMatrixTest.class.getMethod("testGetAdjacentIndices", JsonParameterSet.class)
    );

    public static final Criterion H2_1_3 = createCriterion(
            "Die Methode [[[hasEdge]]] der Klasse [[[AdjacencyMatrix]]] funktioniert vollständig korrekt",
            1,
            () -> AdjacencyMatrixTest.class.getMethod("testHasEdge", JsonParameterSet.class)

    );

    public static final Criterion H2_1 = createParentCriterion("2a", "AdjacencyMatrix", H2_1_1, H2_1_2, H2_1_3);

    // H2b) AdjacencyGraph (4 Points)
    public static final Criterion H2_2_1 = createCriterion(
            "Die Methode [[[addNode]]] der Klasse [[[AdjacencyGraph]]] funktioniert vollständig korrekt",
            1,
            () -> AdjacencyGraphTest.class.getMethod("testAddNode", JsonParameterSet.class)
    );

//    public static final Criterion H2_2_2 = createUntestedCriterion("Die Methode [[[addEdge]]] der Klasse [[[AdjacencyGraph]]] funktioniert vollständig korrekt", 1);
//
//    public static final Criterion H2_2_3 = createUntestedCriterion("Die Methode [[[getEdge]]] der Klasse [[[AdjacencyGraph]]] funktioniert vollständig korrekt", 1);

    public static final Criterion H2_2_2 = createCriterion("Die Methode [[[addEdge]]] der Klasse [[[AdjacencyGraph]]] funktioniert vollständig korrekt", 2,
            () -> AdjacencyGraphTest.class.getMethod("testAddEdge", JsonParameterSet.class)
    );

    public static final Criterion H2_2_3 = createCriterion("Die Methode [[[getEdge]]] der Klasse [[[AdjacencyGraph]]] funktioniert vollständig korrekt", 2,
            () -> AdjacencyGraphTest.class.getMethod("testGetEdge", JsonParameterSet.class)
    );
    public static final Criterion H2_2_4 = createCriterion(
            "Der Konstruktor der Klasse [[[AdjacencyGraph]]] funktioniert vollständig korrekt",
            1,
            () -> AdjacencyGraphTest.class.getMethod("testConstructor", JsonParameterSet.class)
    );

    public static final Criterion H2_2 = createParentCriterion("2b", "AdjacencyGraph", H2_2_1, H2_2_2, H2_2_3, H2_2_4);

    public static final Criterion H2 = createParentCriterion("2", "Graphenrepräsentationen", H2_1, H2_2);

    // ====================================================================
    // H3 - MST (8 Points)
    // ====================================================================

    // H3, 1a (1P) - Testing "sorted" method from class "KruskalSolver"
    public static final Criterion H3_1_a =
            createCriterion(
                    "Die Methode [[[sorted]]] der Klasse [[[KruskalSolver]]] funktioniert vollständig korrekt",
                    1,
                    () -> KruskalTest.class.getMethod("testSorted", JsonParameterSet.class)
            );


    // H3, 1b (1P) - Testing "union" method from class "KruskalSolver"
    public static final Criterion H3_1_b =
            createCriterion(
                    "Die Methode [[[union]]] der Klasse [[[KruskalSolver]]] funktioniert vollständig korrekt",
                    1,
                    () -> KruskalTest.class.getMethod("testUnion", JsonParameterSet.class)
            );

    // H3, 1c (1P) - Testing "extractMin" method from class "PrimSolver"
    public static final Criterion H3_1_c =
            createCriterion(
                    "Die Methode [[[extractMin]]] der Klasse [[[PrimTest]]] funktioniert vollständig korrekt",
                    1,
                    () -> PrimTest.class.getMethod("testExtractMin", JsonParameterSet.class)
            );

    // H3, 1 - Testing the Task 1 (Vorbereitung)
    public static final Criterion H3_1 =
            createParentCriterion("3_1", "Sorted, Union und ExtractMin", H3_1_a, H3_1_b, H3_1_c);

    // H3 - Testing for Task H3
    public static final Criterion H3 =
            createParentCriterion("3", "Minimale Spannbäume", H3_1);


    // ====================================================================
    // H4 - NETWORK FLOW (8 Points)
    // ====================================================================

    public static Criterion H4_1_1 = createCriterion(
		"Die Methode [[[buildFlowNetwork]]] der Klasse [[[SolveProcedure]]] kann korrekt ein Netzwerk aufbauen, wenn es möglich ist, dass der Zielspieler zu Topspielern gehört.",
		1,
		() -> SolveProcedureTest.class.getMethod("testbuildFlowNetworkWin", JsonParameterSet.class)
    );

    public static Criterion H4_1_2 = createCriterion(
		"Die Methode [[[buildFlowNetwork]]] der Klasse [[[SolveProcedure]]] kann korrekt ein Netzwerk aufbauen, auch wenn es nicht möglich ist, dass der Zielspieler zu Topspielern gehört.",
		2,
		() -> SolveProcedureTest.class.getMethod("testbuildFlowNetworkLoseCanBuild", JsonParameterSet.class)
    );

    public static Criterion H4_1_3 = createUntestedCriterion(
		"Die Methode [[[buildFlowNetwork]]] der Klasse [[[SolveProcedure]]] identifiziert korrekt Fälle, in denen kein konsistentes Netzwerk aufgebaut werden kann (z.B. aufgrund von negativen Kapazitäten) und gibt entsprechend false zurück.",
		3
    );

    public static Criterion H4_1 = createParentCriterion(
        "H4.1", 
        "Flussnetzwerkaufbau: Die Methode [[[buildFlowNetwork]]] der Klasse [[[SolveProcedure]]] funktioniert vollständig korrekt", 
        H4_1_1, H4_1_2, H4_1_3);

    public static Criterion H4_2_1 = createUntestedCriterion(
		"Die Methode [[[solver]]] der Klasse [[[SolveProcedure]]] liefert korrekt true zurück, wenn der Zielspieler unter optimalen zukünftigen Ergebnissen zu den Topspielern gehören kann.",
		3
    );

	public static Criterion H4_2_2 = createCriterion(
		"Die Methode [[[solver]]] der Klasse [[[SolveProcedure]]] liefert korrekt false zurück, wenn der Zielspieler unter optimalen zukünftigen Ergebnissen nicht zu den Topspielern gehören kann.",
		3,
		() -> SolveProcedureTest.class.getMethod("testSolverLose", JsonParameterSet.class)
	);
    
    public static final Criterion H4_2 = createParentCriterion(
		"H4.2", 
		"Solver: Die Methode [[[solver]]] der Klasse [[[SolveProcedure]]] funktioniert vollständig korrekt", 
		H4_2_1, H4_2_2);

    public static Criterion H4 = createParentCriterion("H4", "Flussnetzwerk", H4_1, H4_2);

    // ========== Rubric Definition ==========
    public static final Rubric RUBRIC = Rubric.builder()
            .title("P3 Public Tests")
            .addChildCriteria(H1)
            .addChildCriteria(H2)
            .addChildCriteria(H3)
            .addChildCriteria(H4)
            .build();
    @Override
    public Rubric getRubric() {
        return Rubric.builder()
                .title("P3 - Graphen")
                .addChildCriteria(H1)
                .addChildCriteria(H2, H3, H4)
                .build();
    }
}