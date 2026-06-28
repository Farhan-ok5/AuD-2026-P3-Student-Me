package p3;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.api.Timeout;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSet;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSetTest;

import static util.AssertionUtil.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.concurrent.TimeUnit;

import p3.flow.*;

@TestForSubmission
public class SolveProcedureTest extends P3_TestBase {

    static final int TIME_LIMIT_SECONDS = 15;
    
    @Override
    public String getTestedClassName() {
        return "SolveProcedure";
    }

    @Override
    public List<String> getOptionalParams() {
        return List.of("namesListSize", "knownGames", "chosenOne", "expected");
    }

    @ParameterizedTest
    @Timeout(value = TIME_LIMIT_SECONDS, unit = TimeUnit.SECONDS)
    @JsonParameterSetTest(value = "flow/solverLoseData.json")
    public void testSolverLose(JsonParameterSet params) {
        Context.Builder<?> context = createContext(params, "solver");
        solverTestRunner(params, context);
    }

    private void solverTestRunner(JsonParameterSet params, Context.Builder<?> context) {
        SolveProcedure temp = callObject(SolveProcedure::new, context, "constructor");

        int namesListSize = params.get("namesListSize");
        List<List<String>> partialGameResults = params.get("knownGames");
        
        List<String> playerNames = playerNamesGenerator(namesListSize);
        
        String name = playerNames.get(Integer.parseInt(params.get("chosenOne")));
        boolean expected = params.get("expected");
        boolean actual = callObject(() -> temp.solver(playerNames, convertToGamesList(playerNames, partialGameResults), name), context, "solver");

        context.add("actual", actual);

        assertEquals(expected, actual, context, "The solver method returned an unexpected result.");
    }

    @ParameterizedTest
    @Timeout(value = TIME_LIMIT_SECONDS, unit = TimeUnit.SECONDS)
    @JsonParameterSetTest(value = "flow/buildFlowNetworkLoseCanBuildData.json")
    public void testbuildFlowNetworkLoseCanBuild(JsonParameterSet params) {
        Context.Builder<?> context = createContext(params, "buildFlowNetwork");
        buildFlowNetworkTestRunner(params, context);
    }

    @ParameterizedTest
    @Timeout(value = TIME_LIMIT_SECONDS, unit = TimeUnit.SECONDS)
    @JsonParameterSetTest(value = "flow/buildFlowNetworkWinData.json")
    public void testbuildFlowNetworkWin(JsonParameterSet params) {
        Context.Builder<?> context = createContext(params, "buildFlowNetwork");
        buildFlowNetworkTestRunner(params, context);
    }

    private void buildFlowNetworkTestRunner(JsonParameterSet params, Context.Builder<?> context) {
        SolveProcedure temp = callObject(SolveProcedure::new, context, "constructor");

        int namesListSize = params.get("namesListSize");
        List<List<String>> partialGameResults = params.get("knownGames");

        List<String> playerNames = playerNamesGenerator(namesListSize);
        List<Game> knownGames = convertToGamesList(playerNames, partialGameResults);

        List<Game> remainingGames = getRemainingGames(playerNames, knownGames);
        Map<String, Integer> currentScores = calculateCurrentScores(playerNames, knownGames);

        String name = playerNames.get(Integer.parseInt(params.get("chosenOne")));
        boolean expected = params.get("expected");
        boolean actual = callObject(() -> {
            var m = SolveProcedure.class.getDeclaredMethod(
                "buildFlowNetwork",
                List.class, Map.class, List.class, String.class
            );
            m.setAccessible(true);
            return (Boolean) m.invoke(temp, playerNames, currentScores, remainingGames, name);
        }, context, "buildFlowNetwork");

        assertEquals(expected, actual, context, "The buildFlowNetwork method returned an unexpected result.");
        
        NetworkFlow<String> networkFlow = callObject(() -> {
            var m = SolveProcedure.class.getDeclaredField("networkFlow");
            m.setAccessible(true);
            return (NetworkFlow<String>) m.get(temp);
        }, context, "networkFlow");

        int threshold = params.get("threshold");
        boolean canWin = params.get("canWin");

		if (expected) {
			boolean isValid = isValidNetworkFlow(networkFlow, threshold, canWin);
			context.add("isNetworkFlowValid", isValid);
			assertEquals(true, isValid, context, "The flow network is not correctly built according to the specifications.");
		}
    }

    private boolean isValidNetworkFlow(NetworkFlow<String> networkFlow, int threshold, boolean expected) {
        int maxFlow = networkFlow.maxFlow("SOURCE", "SINK");
        if (maxFlow > threshold) {
            return false;
        }
        return !(expected ^ (maxFlow == threshold));
    }

    private List<Game> getRemainingGames(List<String> playerNames, List<Game> knownGames) {
        Map<String, Boolean> playedGames = knownGames.stream()
            .flatMap(game -> List.of(
                game.getGameKey(false),
                game.getGameKey(true)
            ).stream())
            .collect(Collectors.toMap(game -> game, game -> true));
        
        List<Game> res = new ArrayList<>();
        for (int i = 1; i < playerNames.size(); i++) {
            for (int j = 0; j < i; j++) {
                String player1 = playerNames.get(i);
                String player2 = playerNames.get(j);
                Game game = new Game(player1, player2);
                if (!playedGames.containsKey(game.getGameKey(false)) && !playedGames.containsKey(game.getGameKey(true))) {
                    res.add(game);
                }
            }
        }
        return res;
    }

    private Map<String, Integer> calculateCurrentScores(List<String> playerNames, List<Game> games) {
        Map<String, Integer> scores = new HashMap<>();

        for (String name : playerNames) {
            scores.put(name, 0);
        }

        for (Game game : games) {
            String player1 = game.getPlayer1();
            String player2 = game.getPlayer2();
            
            scores.put(player1, scores.get(player1) + game.getPlayer1Result().getScore());
            scores.put(player2, scores.get(player2) + game.getPlayer2Result().getScore());
        }

        return scores;
    }

    private List<Game> convertToGamesList(List<String> playerNames, List<List<String>> gamesData) {
        return gamesData.stream()
            .map(data -> new Game(
                    playerNames.get(Integer.parseInt(data.get(0))), 
                    playerNames.get(Integer.parseInt(data.get(1))), 
                    convertToResult(data.get(2)))
                )
            .toList();
    }

    private Game.Result convertToResult(String resultData) {
        switch (resultData) {
            case "WIN" -> {
                return Game.Result.WIN;
            }
            case "LOSE" -> {
                return Game.Result.LOSE;
            }
            case "DRAW" -> {
                return Game.Result.DRAW;
            }
            default -> {return null;}
        }
    }

    private List<String> playerNamesGenerator(int size) {
        List<String> res = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            res.add("Player" + i);
        }
        return res;
    }
}
