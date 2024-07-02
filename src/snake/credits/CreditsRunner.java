package snake.credits;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Provides a panel with static text. Used for a header or footer.
 */
class StaticTextPanel extends JPanel {
    private String msg;
    private Font font;
    private FontMetrics fontMetrics;

    StaticTextPanel(String msg, Font font) {
        this.msg = msg;
        this.font = font;
        fontMetrics = getFontMetrics(font);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawText(g);
    }

    /**
     * Draws text inside the panel.
     * @param g graphic object, provided by context
     */
    private void drawText(Graphics g) {
        Toolkit.getDefaultToolkit().sync();
        g.setColor(Color.white);
        g.setFont(font);
        // places text in the center of the panel
        int textPositionX = (this.getWidth() - fontMetrics.stringWidth(msg))/2;
        int textPositionY = (this.getHeight() + font.getSize())/2;
        g.drawString(msg, textPositionX, textPositionY);
    }

    /**
     * Changes the text of the panel
     * @param msg Text to display. Shouldn't be too long to fit the panel
     */
    void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * Changes font of the static text message
     * @param font the desired <code>Font</code> for this component
     */
    @Override
    public void setFont(Font font) {
        this.font = font;
        fontMetrics = getFontMetrics(font);
    }
}

/**
 * Provides a panel with moving text. Used for as a body panel.
 * The text to be displayed must be provided as separate lines within the collection.
 */
class RunningTextPanel extends JPanel {
    private Collection<String> strings;
    private Font font;
    private FontMetrics fontMetrics;
    private int ofset;

    RunningTextPanel(Collection<String> strings, Font font) {
        this.strings = strings;
        this.font = font;
        fontMetrics = getFontMetrics(font);
        initShift();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawText(g);
    }

    /**
     * Moves text one step up.
     */
    void move() {
        synchronized (this) {
            if (ofset > -strings.size()*font.getSize()) {
                ofset--;
            } else initShift();
        }
        repaint();
    }

    /**
     * Draws text inside the panel.
     * @param g graphic object, provided by context
     */
    private void drawText(Graphics g) {
        Toolkit.getDefaultToolkit().sync();
        g.setColor(Color.white);
        g.setFont(font);
        int stringHeight = font.getSize();
        int string = 0;
        // Calculates the position of each row with an offset and draws a text line
        for (String msg : strings) {
            int textPositionX = (this.getWidth() - fontMetrics.stringWidth(msg)) / 2;
            int textPositionY = (string++ * stringHeight + ofset);
            g.drawString(msg, textPositionX, textPositionY);
        }
    }

    /**
     * Sets font of the text
     * @param font the desired <code>Font</code> for this component
     */
    @Override
    public void setFont(Font font) {
        this.font = font;
        fontMetrics = getFontMetrics(font);
    }

    /**
     * Changes text of running credits
     * @param message Text lines shouldn't be too long to fit the panel
     */
    void setMsg(Collection<String> message) {
        this.strings = message;
        initShift();
    }

    /**
     * Initialise the offset of text lines. Used each time then that lines changed.
     */
    private void initShift(){
        ofset =this.getHeight();
    }
}

/**
 * This class is used to display moving credits with the best scores of players after the game is finished.
 * Provides JPanel with a header, body and footer. Credits moving inside the body
 */
public class CreditsRunner extends JPanel {
    StaticTextPanel northPane, southPane;
    RunningTextPanel centerPane;
    public CreditsRunner() {
        setPreferredSize(new Dimension(400,390));
        initWindow(this);
        initTimer();
    }

    /**
     * Puts all panels inside the container
     * @param container root JPanel
     */
    private void initWindow(Container container) {
        Font font = new Font("Helvetica", Font.BOLD, 15);
        northPane = new StaticTextPanel("Scores = 0", font);
        northPane.setBackground(Color.BLACK);
        northPane.setPreferredSize(new Dimension(400, 20));
        southPane = new StaticTextPanel("Best scores = 100", font);
        southPane.setBackground(Color.BLACK);
        southPane.setPreferredSize(new Dimension(400, 20));

        List<String> Strings = new ArrayList<>();
        centerPane = new RunningTextPanel(Strings, font);
        centerPane.setBackground(Color.BLACK);
        centerPane.setPreferredSize(new Dimension(400, 340));

        setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.add(northPane);
        container.add(centerPane);
        container.add(southPane);
        container.setBackground(Color.BLACK);
    }

    /**
     * Moves text within the body one step per tick
     */
    private void initTimer() {
        int delay = 50;                                     //Sets the speed oh the text scrolling
        ActionListener listener = e -> centerPane.move();
        Timer timer = new Timer(delay, listener);
        timer.start();
    }

    /**
     * Changes text of the header
     * @param header Shouldn't be too long to fit the panel
     */
    public void setHeader(String header) {
        northPane.setMsg(header);
    }

    /**
     * Changes text of the footer
     * @param footer Shouldn't be too long to fit the panel
     */
    public void setFooter(String footer) {
        southPane.setMsg(footer);
    }

    /**
     * Changes text of running credits
     * @param credits Each of the lines shouldn't be too long to fit the panel
     */
    public void setCredits(Collection<String> credits) {
        centerPane.setMsg(credits);
    }
}