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
	
	// ====================================================================
    // H1 - BELLMAN-FORD (5 Points)
    // ====================================================================

    public static final Criterion H1_BF_1 = createCriterion(
        "Die Methode [[[initSSSP]]] der Klasse [[[BellmanFordPathCalculator]]] initialisiert die Maps [[[distances]]] und [[[predecessors]]] korrekt.",
        1,
        () -> BellmanFordPathCalculatorTest.class.getMethod("testInitSSSP", JsonParameterSet.class)
    );

    public static final Criterion H1_BF_2 = createCriterion(
        "Die Methode [[[relax]]] der Klasse [[[BellmanFordPathCalculator]]] aktualisiert [[[distances]]] und [[[predecessors]]] korrekt.",
        1,
        () -> BellmanFordPathCalculatorTest.class.getMethod("testRelax", JsonParameterSet.class)
    );

    public static final Criterion H1_BF_3 = createUntestedCriterion(
        "Die Methode [[[processGraph]]] der Klasse [[[BellmanFordPathCalculator]]] berechnet die kürzesten Distanzen vom Startknoten korrekt.",
        1
    );

    public static final Criterion H1_BF_4 = createCriterion(
        "Die Methode [[[hasNegativeCycle]]] der Klasse [[[BellmanFordPathCalculator]]] erkennt negative Zyklen korrekt und liefert in zyklenfreien Graphen [[[false]]] zurück.",
        1,
        () -> BellmanFordPathCalculatorTest.class.getMethod("testHasNegativeCycle", JsonParameterSet.class)
    );

    public static final Criterion H1_BF_5 = createUntestedCriterion(
        "Die Methode [[[calculatePath]]] der Klasse [[[BellmanFordPathCalculator]]] liefert den korrekten Pfad zurück und wirft eine [[[CycleException]]], wenn ein negativer Zyklus existiert.",
        1
    );

    public static final Criterion H1_BF = createParentCriterion("1", "Bellman-Ford", H1_BF_1, H1_BF_2, H1_BF_3, H1_BF_4, H1_BF_5);

    // ====================================================================
    // H2 - DIJKSTRA (5 Points)
    // ====================================================================

    public static final Criterion H2_D_1 = createCriterion(
        "Die Methode [[[initSSSP]]] der Klasse [[[DijkstraPathCalculator]]] initialisiert die Maps [[[distances]]] und [[[predecessors]]] korrekt.",
        1,
        () -> DijkstraPathCalculatorTest.class.getMethod("testInitSSSP", JsonParameterSet.class)
    );

    public static final Criterion H2_D_2 = createCriterion(
        "Die Methode [[[relax]]] der Klasse [[[DijkstraPathCalculator]]] aktualisiert [[[distances]]] und [[[predecessors]]] korrekt.",
        1,
        () -> DijkstraPathCalculatorTest.class.getMethod("testRelax", JsonParameterSet.class)
    );

    public static final Criterion H2_D_3 = createUntestedCriterion(
        "Die Methode [[[extractMin]]] der Klasse [[[DijkstraPathCalculator]]] liefert den Knoten mit kleinster Distanz und entfernt ihn aus [[[Q]]].",
        1
    );

    public static final Criterion H2_D_4 = createUntestedCriterion(
        "Die Methode [[[processGraph]]] der Klasse [[[DijkstraPathCalculator]]] berechnet die kürzesten Distanzen vom Startknoten korrekt.",
        1
    );

    public static final Criterion H2_D_5 = createUntestedCriterion(
        "Die Methode [[[calculatePath]]] der Klasse [[[DijkstraPathCalculator]]] liefert den korrekten Pfad zurück.",
        1
    );

    public static final Criterion H2_D = createParentCriterion("2", "Dijkstra", H2_D_1, H2_D_2, H2_D_3, H2_D_4, H2_D_5);

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

    public static final Criterion H3_2_a =
            createUntestedCriterion(
                    "Die Methode [[[solve]]] der Klasse [[[KruskalSolver]]] funktioniert vollständig korrekt",
                    2
            );


    public static final Criterion H3_2_b =
            createUntestedCriterion(
                    "Die Methode [[[getResultAsAdjacencyMatrix]]] der Klasse [[[KruskalSolver]]] funktioniert vollständig korrekt",
                    1
            );


    public static final Criterion H3_2 =
            createParentCriterion("3_2", "Kruskal", H3_2_a, H3_2_b);


    public static final Criterion H3_3 =
            createCriterion(
                    "Die Methode [[[solve]]] der Klasse [[[PrimSolver]]] funktioniert vollständig korrekt",
                    2,
                    () -> PrimTest.class.getMethod("testSolve", JsonParameterSet.class)
            );

    // H3 - Testing for Task H3
    public static final Criterion H3 =
            createParentCriterion("3", "Minimale Spannbäume", H3_1, H3_2, H3_3);


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
                "4.1",
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
		"4.2",
		"Solver: Die Methode [[[solver]]] der Klasse [[[SolveProcedure]]] funktioniert vollständig korrekt",
		H4_2_1, H4_2_2);


    public static Criterion H4 = createParentCriterion("4", "Flussnetzwerk", H4_1, H4_2);


    // ========== Rubric Definition ==========
    public static final Rubric RUBRIC = Rubric.builder()
            .title("P3 Public Tests")
            .addChildCriteria(H1_BF)
            .addChildCriteria(H2_D)
            .addChildCriteria(H3)
            .addChildCriteria(H4)
            .build();
    @Override
    public Rubric getRubric() {
        return Rubric.builder()
                .title("P3 - Graphen")
                .addChildCriteria(H1_BF, H2_D)
                .addChildCriteria(H3, H4)
                .build();
    }
}