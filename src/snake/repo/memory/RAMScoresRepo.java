package snake.repo.memory;

import snake.ScoresRepo;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RAMScoresRepo implements ScoresRepo {
    Map<String, Integer> map = new HashMap<>();
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
}
