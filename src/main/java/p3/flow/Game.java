package p3.flow;

/**
 * Represents a pairwise game between two players.
 *
 * A Game may carry a {@link Result} for played matches; when result is null the game is treated as unplayed.
 *
 * Notes on usage:
 *  - getPlayer1Result() may return null for unplayed games; callers that assume a result should check for null first.
 */
public class Game {

    /**
     * Possible outcomes for a team in a game with their associated point value.
     * WIN = 2, DRAW = 1, LOSE = 0.
     */
    static public enum Result {
        WIN(2), DRAW(1), LOSE(0);

        private final int score;

        Result(int score) {
            this.score = score;
        }

        /**
         * @return the number of points awarded to a team for this {@link Result} value
         */
        public int getScore() {
            return score;
        }
    }

    private static final String DELIMITER = "$";

    private final String player1;
    private final String player2;
    private Result result;

    /**
     * Construct a played game record.
     *
     * @param player1 first player's identifier (owner of the Result value)
     * @param player2 second player's identifier
     * @param result outcome for player1 (must be non-null)
     */
    public Game(String player1, String player2, Result result) {
        this.player1 = player1;
        this.player2 = player2;
        this.result = result;
    }

    /**
     * Construct an unplayed game between two players (result initially unset).
     *
     * @param player1 first player's identifier
     * @param player2 second player's identifier
     */
    public Game(String player1, String player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.result = null;
    }

    /**
     * @return the first player's identifier
     */
    public String getPlayer1() {
        return player1;
    }

    /**
     * @return the second player's identifier
     */
    public String getPlayer2() {
        return player2;
    }

    /**
     * Get {@link Result} for the first player.
     *
     * @return the stored {@link Result}, or null if the game is unplayed
     */
    public Result getPlayer1Result() {
        return result;
    }

    /**
     * Get {@link Result} for the second player.
     *
     * @return the {@link Result} for the second player (inverse of the first player's result)
     * @throws NullPointerException if the internal result is null (unplayed game)
     */
    public Result getPlayer2Result() {
        switch (result) {
            case WIN    : return Result.LOSE;
            case LOSE   : return Result.WIN;
            case DRAW   : return Result.DRAW;
            default     : throw new IllegalStateException("Unexpected value: " + result);
        }
    }

    /**
     * Set the result for this game. Only allowed once.
     *
     * @param result the {@link Result} to assign (non-null)
     * @throws IllegalStateException if a result was already assigned
     */
    public void setResult(Result result) {
        if (this.result != null) {
            throw new IllegalStateException("Result already set");
        }
        this.result = result;
    }

    /**
     * Generate a unique key for this game based on the player identifiers and a specified ordering.
     * The key is generated as "player1$player2" if isOneTwo is true, and "player2$player1" otherwise.
     * 
     * @param isOneTwo determines the ordering of player identifiers in the generated key
     * @return a unique string key representing this game based on the player identifiers and the specified ordering
     */
    public String getGameKey(boolean isOneTwo) {
        if (isOneTwo) {
            return player1 + DELIMITER + player2;
        } else {
            return player2 + DELIMITER + player1;
        }
    }

    @Override
    public String toString() {
        switch (result) {
            case WIN    : return player1 + " wins against " + player2;
            case LOSE   : return player2 + " wins against " + player1;
            case DRAW   : return player1 + " draws with " + player2;
            default     : throw new IllegalStateException("Unexpected value: " + result);
        }
    }
}
