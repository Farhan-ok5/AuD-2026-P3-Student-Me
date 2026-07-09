package p3.flow;

import static org.tudalgo.algoutils.student.Student.crash;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * Solver that reduces the tournament feasibility question to a max-flow problem.
 *
 * Note: several helper methods assume that {@link Game} objects used to compute current scores contain a non-null
 * Result; passing games without results will cause a {@link NullPointerException} when scores are aggregated.
 */
public class SolveProcedure {

    public final NetworkFlow<String> networkFlow;
    private static final String SOURCE = "SOURCE";
    private static final String SINK = "SINK";

    public SolveProcedure() {
        this.networkFlow = new NetworkFlow<>();
    }

    /**
     * Clear the flow network to prepare for a new solver run.
     */
    private void resetNetwork() {
        this.networkFlow.reset();
    }

    /**
     * Compute current total points per player from already played games.
     *
     * @param players list of all player identifiers; each name is included as a key in the returned map
     * @param partialGameResults list of played {@link Game} objects; each {@link Game} must have a non-null result
     * @return a Map mapping each player name to their current integer score
     * @throws NullPointerException if any Game in partialGameResults has a null result (score aggregation assumes non-null)
     */
    public Map<String, Integer> getCurrentTeamScore(final List<String> players, final List<Game> partialGameResults) {
        Map<String, Integer> res = new HashMap<>();
        for (String player : players) {
            res.put(player, 0);
        }
        for (Game game : partialGameResults) {
            String player1 = game.getPlayer1();
            String player2 = game.getPlayer2();
            res.put(player1, res.get(player1) + game.getPlayer1Result().getScore());
            res.put(player2, res.get(player2) + game.getPlayer2Result().getScore());
        }
        return res;
    }

    /**
     * Enumerate unplayed pairwise games.
     *
     * @param players ordered list of all player identifiers used to generate unique pairs (i<j)
     * @param partialGameResults list of already played games used to detect and skip existing pairs
     * @return list of {@link Game} objects representing every unordered pair not present in {@code partialGameResults}; these {@link Game} instances may have null results
     */
    public List<Game> getRemainingGames(final List<String> players, final List<Game> partialGameResults) {
        Map<String, Boolean> gameExists = new HashMap<>();
        for (Game game : partialGameResults) {
            gameExists.put(game.getGameKey(true), true);
            gameExists.put(game.getGameKey(false), true);
        }
        List<Game> res = new ArrayList<>();
        for (int i = 0; i < players.size(); i++) {
            for (int j = i + 1; j < players.size(); j++) {
                String player1 = players.get(i);
                String player2 = players.get(j);
                Game game = new Game(player1, player2);
                if (!gameExists.containsKey(game.getGameKey(true))) {
                    res.add(game);
                }
            }
        }
        return res;
    }

    /**
     * Compute the maximum additional points the specified player can still obtain from remaining games.
     *
     * @param remainingGames list of remaining unplayed {@link Game} (results unset)
     * @param name the player identifier whose future maximum is to be computed
     * @return the maximal extra points achievable (2 points per remaining game involving {@code name})
     */
    public int getMaxScore(List<Game> remainingGames, String name) {
        int maxScore = 0;
        for (Game game : remainingGames) {
            if (game.getPlayer1().equals(name) || game.getPlayer2().equals(name)) {
                maxScore += 2;
            }
        }
        return maxScore;
    }

    /**
     * <p>Build the flow network representing allocation of points from remaining games and store it in {@link #networkFlow}.</p>
     *
     * <p>
     * Examplified Topology:
     * <pre>
     *       /--(2)-> game1 -(2)-> playerA -(1)--\v
     * SOURCE              \-(2)-> playerB -(3)-> SINK
     *       \             /-(2)--/^             /^
     *        \-(2)-> game2 -(2)-> playerC -(2)-/
     * </pre>
     * </p>
     *
     * <p>
     * Important notes / possible errors:
     *  - The method assumes that the input lists are consistent (e.g., {@code remainingGames} should not contain games with known result, {@code currentTeamScore} should contain entries for all players, etc.). Inconsistent inputs may lead to incorrect network construction or runtime exceptions.
     *  - The network should be reset before building to avoid residual edges from previous runs; this is handled by the call to {@code resetNetwork()} at the start of the method.
     * </p>
     *
     * @param players list of all player identifiers
     * @param currentTeamScore map of current known scores per player
     * @param remainingGames list of unplayed games (results expected to be null for these)
     * @param name the target player identifier to be tested for top-scorer feasibility
	 * @return true if the residual network can be built without any inconsistencies (e.g. negative capacities), false otherwise
     */
    public boolean buildFlowNetwork(final List<String> players, final Map<String, Integer> currentTeamScore, final List<Game> remainingGames, final String name) {
        //TODO: H4.1
        //networkFlow.reset();
        int scoreX = currentTeamScore.get(name);
        int maxExtrapointsX = 0;

        // spielerX gewinnt all seine restlichen spiele
        for (Game game : remainingGames) {
            if (name.equals(game.getPlayer1()) || name.equals(game.getPlayer2())) {
                maxExtrapointsX += 2;
            }
        }
        int maxPossiblePointsX = scoreX + maxExtrapointsX;

        // andere spieler können keine negativen Punkte erhalten
        for (String player : players) {
            if (player.equals(name)) {
                continue;
            }
            int playerScore = currentTeamScore.get(player);
            int allowedExtraPoints = maxPossiblePointsX - playerScore;

            // PX + M - Pi darf nicht negativ sein sonst hat X verloren
            if (allowedExtraPoints < 0) {
                return false;
            }
        }

        // 10min
        // Reminder: graph without X
        //
        //                A                B                  C
        //                |     Game 1     |     Player 1     |
        //                |      ...       |       ...        |
        //     SOURCE     |      ...       |       ...        |     SINK
        //                |      ...       |       ...        |
        //                |     Game n     |     Player m     |
        //                ^                ^                  ^
        //   EdgesCount:  n               2*n                 m

        String source = "SOURCE";
        String sink = "SINK";

        // Edges at C
        for (String player : players) {
            if (player.equals(name)) {
                continue;
            }
            int allowedExtraPoints = maxPossiblePointsX - currentTeamScore.get(player);
            networkFlow.addEdge(player, sink, allowedExtraPoints);
        }

        int gameIndex = 0;
        for (Game game : remainingGames) {
            String p1 = game.getPlayer1();
            String p2 = game.getPlayer2();

            if (!name.equals(p1) && !name.equals(p2)) {
                String gameNode = "Game_" + gameIndex;
                // Edge at A
                networkFlow.addEdge(source, gameNode, 2);
                // Edges at B
                networkFlow.addEdge(gameNode, p1, 2);
                networkFlow.addEdge(gameNode, p2, 2);
            }
            gameIndex++;
        }

        return true;
    }

    /**
     * Decide whether player {@code name} can still finish as one of the top scorers under optimal future play.
     *
     * @param players list of all player identifiers participating in the tournament
     * @param partialGameResults list of already played games; each {@link Game} used for score computation must have a non-null result
     * @param name the player to test for top-scorer feasibility
     * @return {@code true} if, under some assignments of results in the remaining games, {@code name} can be a top scorer; {@code false} otherwise
     * @throws NullPointerException if {@code partialGameResults} contains {@link Game} objects with null results (score aggregation assumes non-null)
     */
    public boolean solver(final List<String> players, final List<Game> partialGameResults, final String name) {
        // H4.2 - TODO
        crash("Not implemented yet");
        return false;
    }
}
