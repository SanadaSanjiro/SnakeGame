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

import cls.ClassDB;
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
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Arena extends JPanel implements ActionListener {
InputStream in;
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
    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = true;
    String name ="";
    private Timer timer;
    private Image ball;
    private Image drink;
    private Image head;

    public Arena() {
        name();
        addKeyListener(new TAdapter());
        setBackground(Color.black);
        setFocusable(true);
        setPreferredSize(new Dimension(arena_width, arena_height));
        loadImages();
        initGame();
    }

    private void name() {
        name = JOptionPane.showInputDialog(this, "Please Input Your Name:");
        if (name == null) {
            name = "Player1";
            //System.exit(0);
        } else {
            if (name.isEmpty())
            {
                JOptionPane.showMessageDialog(this,"Name Already Exist!");
                name();
            } else {
                try
                {
                    Connection c=ClassDB.getkoneksi();
                    Statement st=c.createStatement();
                    String nameCheckQuery="Select * from score where nama = '" + name +"'";
                    ResultSet r=st.executeQuery(nameCheckQuery);
                    if (!r.next())
                    {
                        try
                        {
                            st.executeUpdate("Insert into score(nama) values('" + name + "')");
                        } catch(Exception e) {
                            System.out.println(e.getMessage());
                        }
                    }
                } catch(Exception e) { System.out.println(e.getMessage()); }
            }
        }
    }

    private void loadImages() {

        ImageIcon iid = new ImageIcon("src/dot.png");
        ball = iid.getImage();

        ImageIcon iia = new ImageIcon("src/minum.png");
        drink = iia.getImage();

        ImageIcon iih = new ImageIcon("src/kanan.png");
        head = iih.getImage();
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
         try {
            Connection c=ClassDB.getkoneksi();
            Statement s=c.createStatement();
            String sqel = "UPDATE score Set score ='" + scores +"' where nama = '" + name + "'";
            s.executeUpdate(sqel);
            /*
            String cektinggi="Select * from score where nama = '" + name.toString() +"'";
            ResultSet r=s.executeQuery(cektinggi);
            boolean isResult = r.next();
            if (isResult)
            {
                String topName = r.getString("nama");
                System.out.println(topName);
                String topScores = r.getString("score");
                System.out.println(topScores);
                highestScore = Integer.parseInt(topScores);
                if (scores <= highestScore) {return;}
                else
                {
                    String sqel = "UPDATE score Set score ='" + scores +"' where nama = '" + name.toString()+ "'";
                    s.executeUpdate(sqel);
                }
             */
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
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
        
        try {            
            Connection c=ClassDB.getkoneksi();
            Statement s= c.createStatement();
            String sql="Select * from score where score = (select max(score) from score)";
            ResultSet r=s.executeQuery(sql);
            if (r.next()){
                highscore = Integer.parseInt(r.getString("score"));
                String scr = "Biggest Score "+r.getString("nama")+" = "+highscore;
                g.drawString(scr, (arena_width - metr.stringWidth(scr)) -10, arena_height -5);
            }
            else {
                String scr = "Biggest Score = 0";
               g.drawString(scr, (arena_width - metr.stringWidth(scr)) -10, arena_height -5);
            }
            r.close();
            s.close();
            } catch(Exception e) { System.out.println(e.getMessage()); }
        } else { gameOver(g);}
    }
    private void gameOver(Graphics g) {
        updateScores();
        if (scores <= highscore) {
            String msg = "Your Score: = "+ scores;
            Font small = new Font("Helvetica", Font.BOLD, 14);
            FontMetrics metr = getFontMetrics(small);

            g.setColor(Color.white);
            g.setFont(small);
         g.drawString(msg, (arena_width - metr.stringWidth(msg)) / 2, arena_height / 2);
        }
        else {
            String msgg = "Congratulation High Score = "+ scores;
            Font small = new Font("Helvetica", Font.BOLD, 14);
            FontMetrics metr = getFontMetrics(small);

            g.setColor(Color.blue);
            g.setFont(small);
            g.drawString(msgg, (arena_width - metr.stringWidth(msgg)) / 2, arena_height / 2);
        }
    }
    
    private void checkDrink() {
        if ((x[0] == drink_x) && (y[0] == drink_y)) {
            snake_length++;
            scores = scores + 5;
            try {
                in = new FileInputStream("src\\slurp.wav");
                //AudioStream audios = new AudioStream(in);
                //AudioPlayer.player.start(audios);
            }
            catch(Exception e) { System.out.println(e.getMessage()); }
            placeDrinks();
        }
    }

    private void move_snake() {
        for (int z = snake_length; z > 0; z--) {
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
        }
        if (leftDirection) {
            x[0] -= ballSize;
        }
        if (rightDirection) {
            x[0] += ballSize;
        }
        if (upDirection) {
            y[0] -= ballSize;
        }
        if (downDirection) {
            y[0] += ballSize;
        }
    }

    private void checkCollision() {
        for (int z = snake_length; z > 0; z--) {
            if ((z > 5) && (x[0] == x[z]) && (y[0] == y[z])) {
                inGame = false;
                try {
                    in = new FileInputStream("src\\beep.wav");
                    //AudioStream audios = new AudioStream(in);
                    //AudioPlayer.player.start(audios);
                } catch(Exception e) { System.out.println(e.getMessage()); }
            }
        }

        if (y[0] >= arena_height) {
            inGame = false;
            try {
                in = new FileInputStream("src\\beep.wav");
                //AudioStream audios = new AudioStream(in);
                //AudioPlayer.player.start(audios);
            } catch(Exception e) { System.out.println(e.getMessage()); }
        }

        if (y[0] < 0) {
            inGame = false;
            try {
                in = new FileInputStream("src\\beep.wav");
                //AudioStream audios = new AudioStream(in);
                //AudioPlayer.player.start(audios);
            }
            catch (Exception e) { System.out.println(e.getMessage()); }
        }

        if (x[0] >= arena_width) {
            inGame = false;
            try {
                in = new FileInputStream("src\\beep.wav");
                //AudioStream audios = new AudioStream(in);
                //AudioPlayer.player.start(audios);
            } catch(Exception e) { System.out.println(e.getMessage()); }
        }

        if (x[0] < 0) {
            inGame = false;
            try {
                in = new FileInputStream("src\\beep.wav");
                //AudioStream audios = new AudioStream(in);
                //AudioPlayer.player.start(audios);
            } catch(Exception e) { System.out.println(e.getMessage()); }
        }
        
        if(!inGame) {
            timer.stop();
        }
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
            if ((key == KeyEvent.VK_LEFT) && (!rightDirection)) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
                ImageIcon kiri = new ImageIcon("src/kiri.png");
                head = kiri.getImage();
            }
            if ((key == KeyEvent.VK_RIGHT) && (!leftDirection)) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
                ImageIcon kanan = new ImageIcon("src/kanan.png");
                head = kanan.getImage();
            }
            if ((key == KeyEvent.VK_UP) && (!downDirection)) {
                upDirection = true;
                rightDirection = false;
                leftDirection = false;
                ImageIcon atas = new ImageIcon("src/atas.png");
                head = atas.getImage();
            }
            if ((key == KeyEvent.VK_DOWN) && (!upDirection)) {
                downDirection = true;
                rightDirection = false;
                leftDirection = false;
                ImageIcon bawah = new ImageIcon("src/bawah.png");
                head = bawah.getImage();
            }
            if (key == KeyEvent.VK_P) {
               if(timer.isRunning()) { timer.stop();
               } else { timer.start(); }
            }
        }
    }
}