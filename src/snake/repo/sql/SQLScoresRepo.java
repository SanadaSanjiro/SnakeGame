package snake.repo.sql;

import snake.repo.ScoresRepo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Implementation of the ScoresRepo interface that stores players' scores in a database.
 * The connection to the database is provided by the ClassDB class.
 */

public class SQLScoresRepo implements ScoresRepo {
    @Override
    public void addPlayer(String name) {
        try (Connection connection = ClassDB.getConnection()) {
            Statement st=connection.createStatement();
            String query="Select * from snake.score where nama = '" + name +"'";
            ResultSet r=st.executeQuery(query);
            if (!r.next()) {
                st.executeUpdate("Insert into snake.score(nama) values('" + name + "')");
            }
        } catch (SQLException e) {
            RuntimeException exception = new RuntimeException("Database error when saving player name");
            exception.addSuppressed(e);
            throw exception;
        }
    }

    @Override
    public void updateScores(String name, int scores) {
        try (Connection connection = ClassDB.getConnection()) {
            Statement st = connection.createStatement();
            String query = "UPDATE snake.score Set score ='" + scores + "' where nama = '" + name + "'";
            st.executeUpdate(query);
        } catch (SQLException e) {
            RuntimeException exception = new RuntimeException("Database error when saving player scores");
            exception.addSuppressed(e);
            throw exception;
        }
    }

    @Override
    public int getTopScores() {
        try (Connection connection = ClassDB.getConnection()) {
            String query="select max(score) as top from snake.score;";
            ResultSet r = processQuery(query, connection);
            if (r.next()) {
                String result = r.getString("top");
                if (Objects.isNull(result)) return 0;
                return r.getInt("top");
            } else { return 0; }
        } catch (SQLException e) {
            RuntimeException exception = new RuntimeException("Database error when getting top scores");
            exception.addSuppressed(e);
            throw exception;
        }
    }

    @Override
    public String getTopPlayer() {
        int topScore = getTopScores();
        try (Connection connection = ClassDB.getConnection()) {
            String query="select nama from snake.score where score = " + topScore;
            ResultSet r = processQuery(query, connection);
            if (r.next()) {
                return r.getString("nama");
            } else { return null; }
        } catch (SQLException e) {
            RuntimeException exception = new RuntimeException("Database error when getting top scores");
            exception.addSuppressed(e);
            throw exception;
        }
    }

    @Override
    public int getPlayersBest(String name) {
        try (Connection connection = ClassDB.getConnection()) {
            String query="Select * from snake.score where nama = '" + name +"'";
            ResultSet r = processQuery(query, connection);
            if (r.next()) {
                return r.getInt("score");
            }
        } catch (SQLException e) {
            RuntimeException exception = new RuntimeException("Database error when getting player's scores");
            exception.addSuppressed(e);
            throw exception;
        }
        return 0;
    }

    private ResultSet processQuery(String query, Connection connection) throws SQLException {
        Statement st=connection.createStatement();
        return st.executeQuery(query);
    }

    @Override
    public Map<String, Integer> getAllScores() {
        Map<String, Integer> map = new HashMap<>();
        try (Connection connection = ClassDB.getConnection()) {
            String query="Select * from snake.score";
            ResultSet r = processQuery(query, connection);
            while (r.next()) {
                String name = r.getString("nama");
                int score = r.getInt("score");
                map.put(name, score);
            }
        } catch (SQLException e) {
            RuntimeException exception = new RuntimeException("Database error when getting player's scores");
            exception.addSuppressed(e);
            throw exception;
        }
        return map;
    }
}
