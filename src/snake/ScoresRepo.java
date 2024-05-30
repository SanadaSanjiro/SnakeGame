package snake;

/**
 * Describes an abstract storage for player names and their best scores.
 */

public interface ScoresRepo {
    /**
     * Adds a player into the storage if it does not exist.
     * @param name Player's name
     */
    void addPlayer(String name);

    /**
     * Updates the player's best scores if the new score is better than the scores previously saved.
     * @param name Player's name
     * @param scores Current scores
     */
    void updateScores(String name, int scores);

    /**
     * Returns the best scores among all players
     * @return Top scores
     */
    int getTopScores();

    /**
     * Returns the top player with the most points
     * @return The name of the top player (String)
     */
    String getTopPlayer();

    /**
     * Returns the player's best scores
     * @param name Player's name
     * @return player's best scores
     */
    int getPlayersBest(String name);
}