/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package snake.repo.sql;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;
/**
 *
 * @author bagong
 * Provides database connection for SQLScoresRepo class
 */
class ClassDB {
    public static Connection getConnection() {
        try {
            String url, user, password;
            url = "jdbc:mysql://localhost:3306/snake";
            user = "snake";
            password = "pass1234";
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException t) {
            JOptionPane.showMessageDialog(null,
                    "The connection to a database not established");
            throw new Error();
        }
    }
}