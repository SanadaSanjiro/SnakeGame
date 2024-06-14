/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package snake;

/*
 *
 * @author NDIAPPINK
 */

import snake.audio.MidiAudioPlayer;
import snake.audio.WaveAudioPlayer;
import snake.repo.memory.RAMScoresRepo;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Objects;
import java.util.Optional;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Arena extends JPanel implements ActionListener {
    private final int arena_width = 400;
    private final int arena_height = 400;
    private final int ballSize = 10;
    private final int ALL_DOTS = 1000;
    private int scores, highscore = 0;
    private final int[] x = new int[ALL_DOTS];
    private final int[] y = new int[ALL_DOTS];
    private int snake_length;
    private int drink_x;
    private int drink_y;
    private boolean inGame = true;
    String name ="";
    private Timer timer;
    private final Image ball = new ImageIcon("resources\\img\\dot.png").getImage();
    private final Image drink = new ImageIcon("resources\\img\\minum.png").getImage();
    private Image head = new ImageIcon("resources\\img\\kanan.png").getImage();
    private final ScoresRepo repo;
    private Direction direction;

    private final WavePlayer wavePlayer = WaveAudioPlayer.getPlayer();
    private final File slurp = new File("resources\\wav\\slurp.wav");
    private final File beep = new File("resources\\wav\\beep.wav");
    private final MidiPlayer midiPlayer = MidiAudioPlayer.getPlayer();

    public Arena() {
        // repo = new SQLScoresRepo(); // remove slashes to use SQL repository for top scores
        repo = new RAMScoresRepo(); // remove slashes to use RAM repository for top scores
        direction = Direction.RIGHT;
        name();
        addKeyListener(new TAdapter());
        setBackground(Color.black);
        setFocusable(true);
        setPreferredSize(new Dimension(arena_width, arena_height));
        initGame();
        File backgroundMusic = new File("resources\\midi\\lemon.mid");
        midiPlayer.playMidi(backgroundMusic);
    }

    /**
     * Shows "Enter your name" dialog window.
     * If the name is blank sets the default name = Player1
     */
    private void name() {
        name = JOptionPane.showInputDialog(this, "Please Input Your Name:");
        if (name == null || name.isEmpty()) { name = "Player1"; }
        repo.addPlayer(name);
    }

    private void initGame() {
        snake_length = 5;
        for (int z = 0; z < snake_length; z++) {
            x[z] = arena_width /20;
            y[z] = arena_height /2;
        }
        placeDrinks();
        int DELAY = 200;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

    private void updateScores() {
        repo.updateScores(name, scores);
    }

    private void doDrawing(Graphics g) {
        if (inGame) {
            g.drawImage(drink, drink_x, drink_y, this);
            for (int z = 0; z < snake_length; z++) {
                if (z == 0) {
                    g.drawImage(head, x[z], y[z], this);
                } else {
                    g.drawImage(ball, x[z], y[z], this);
                }
            }

            Toolkit.getDefaultToolkit().sync();
            String msg = "Score = " + scores;
            Font small = new Font("Helvetica", Font.BOLD, 10);
            FontMetrics metr = getFontMetrics(small);

            g.setColor(Color.white);
            g.setFont(small);
            g.drawString(msg, 5, arena_height - (arena_height -10));
        
            highscore = repo.getTopScores();
            String topName = repo.getTopPlayer();
            String scr;
            if (Objects.nonNull(topName)) {
                scr = "Biggest Score " + topName + " = " + highscore;
            } else {
                scr = "Biggest Score  = 0" + highscore;
            }
            g.drawString(scr, (arena_width - metr.stringWidth(scr)) -10, arena_height -5);
        } else { gameOver(g); }
    }

    private void gameOver(Graphics g) {
        updateScores();
        String msg;
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);
        if (scores <= highscore) {
            msg = "Your Score: = " + scores;
            g.setColor(Color.white);
        }
        else {
            msg = "Congratulation High Score = " + scores;
            g.setColor(Color.blue);
        }
        g.setFont(small);
        g.drawString(msg, (arena_width - metr.stringWidth(msg)) / 2, arena_height / 2);
    }
    
    private void checkDrink() {
        if ((x[0] == drink_x) && (y[0] == drink_y)) {
            snake_length++;
            scores = scores + 5;
            wavePlayer.playWave(slurp);
            placeDrinks();
        }
    }

    private void move_snake() {
        direction.move(x, y, ballSize);
    }

    /**
     * Checks if snake crosses itself or borderlines
     */
    private void checkCollision() {
        for (int z = snake_length; z > 0; z--) {
            if ((z > 5) && (x[0] == x[z]) && (y[0] == y[z])) {
                breakAndBeep();
            }
        }

        if (y[0] >= arena_height || y[0] < 0 || x[0] >= arena_width || x[0] < 0) {
            breakAndBeep();
        }

        if(!inGame) {
            timer.stop();
        }
    }

    /**
     * Makes some sound and stops the game
     */
    private void breakAndBeep() {
        midiPlayer.stop();
        wavePlayer.playWave(beep);
        inGame = false;
    }

    private void placeDrinks() {
        int RAND_POS = 30;
        int r = (int) (Math.random() * RAND_POS);
        drink_x = ((r * ballSize));
        r = (int) (Math.random() * RAND_POS);
        drink_y = ((r * ballSize));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            checkDrink();
            checkCollision();
            move_snake();
        }
        repaint();
    }

    private class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            Optional<Direction> newDirection =  Direction.fromKey(key);
            if (newDirection.isPresent()) {
                direction = newDirection.get();
                head = direction.getImage();
            }
            if (key == KeyEvent.VK_P) {  // Pause the game by P pressed
               if(timer.isRunning()) { timer.stop();
               } else { timer.start(); }
            }
        }
    }
}