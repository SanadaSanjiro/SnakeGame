/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package snake;

/**
 *
 * @author NDIAPPINK
 */
import javax.swing.*;
import java.awt.*;

public class SnakeStarter extends JFrame{
    public SnakeStarter() {
        add(new Arena());
        setResizable(false);
        pack();
        setTitle("Snake");
        ImageIcon icon = new ImageIcon("src/snake.png");
        setIconImage(icon.getImage());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
