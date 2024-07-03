package snake.repo.file;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Provides static methods to serialize and deserialize a Map into a scores.sav file
 */
public class MapSerialaizer {
    private static final String SCORES_FILE = "resources\\saves\\scores.sav";

    /**
     * Saves a map of players' scores to a file using serialization.
     * @param map Map<String, Integer> where String is a players name and Integer is his scores
     */
    public static void serialize(Map<String, Integer> map) {
        try (FileOutputStream myFileOutStream = new FileOutputStream(SCORES_FILE);
             ObjectOutputStream myObjectOutStream = new ObjectOutputStream(myFileOutStream))
        {
            myObjectOutStream.writeObject(map);
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Gets a players score map from a file using deserialization.
     * @return Map<String, Integer> where String is a players name and Integer is his scores
     */
    public static Map<String, Integer> deserialize() {
        HashMap<String, Integer> map = null;
        try (FileInputStream fileInput = new FileInputStream(SCORES_FILE);
             ObjectInputStream objectInput = new ObjectInputStream(fileInput))
        {
            map = (HashMap<String, Integer>)objectInput.readObject();

        }
        catch (IOException | ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }
        return map;
    }

    /**
     * Checks if a Map with a scores already saved to a file
     * @return true if file with a scores already exists
     */
    public static boolean mapFileExists() {
        return new File(SCORES_FILE).exists();
    }
}
