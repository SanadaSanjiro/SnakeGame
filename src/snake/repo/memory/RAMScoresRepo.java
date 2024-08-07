package snake.repo.memory;

import snake.repo.ScoresRepo;

import java.util.*;

/**
 * Implementation of the ScoresRepo interface, which stores players' scores in the computer's memory.
 */

public class RAMScoresRepo implements ScoresRepo {
    private final Map<String, Integer> map = new HashMap<>();
    @Override
    public void addPlayer(String name) {
        map.putIfAbsent(name, 0);
    }

    @Override
    public void updateScores(String name, int scores) {
        map.put(name, scores);
    }

    @Override
    public int getTopScores() {
        Optional<Map.Entry<String, Integer>> optional =  map.entrySet()
                .stream()
                .max(Comparator.comparing(Map.Entry::getValue));
        if (optional.isPresent()) { return optional.get().getValue(); }
        return 0;
    }

    @Override
    public String getTopPlayer() {
        Optional<Map.Entry<String, Integer>> optional =  map.entrySet()
                .stream()
                .max(Comparator.comparing(Map.Entry::getValue));
        if (optional.isPresent()) { return optional.get().getKey(); }
        return null;
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
