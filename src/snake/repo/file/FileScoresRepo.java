package snake.repo.file;

import snake.repo.ScoresRepo;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Implementation of the ScoresRepo interface that stores players' scores in a file using serialization
 */
public class FileScoresRepo implements ScoresRepo {
    private final Map<String, Integer> map;

    public FileScoresRepo() {
        if (MapSerialaizer.mapFileExists()) {
            map = MapSerialaizer.deserialize();
        } else {
            map = new HashMap<>();
        }
    }
    @Override
    public void addPlayer(String name) {
        map.putIfAbsent(name, 0);
        MapSerialaizer.serialize(map);
    }

    @Override
    public void updateScores(String name, int scores) {
        map.put(name, scores);
        MapSerialaizer.serialize(map);
    }

    @Override
    public int getTopScores() {
        Optional<Map.Entry<String, Integer>> optional =  map.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue());
        if (optional.isPresent()) { return optional.get().getValue(); }
        return 0;
    }

    @Override
    public String getTopPlayer() {
        Optional<Map.Entry<String, Integer>> optional =  map.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue());
        return optional.map(Map.Entry::getKey).orElse(null);
    }

    @Override
    public int getPlayersBest(String name) {
        return map.get(name);
    }

    @Override
    public Map<String, Integer> getAllScores() {
        return Map.copyOf(map);
    }
}
