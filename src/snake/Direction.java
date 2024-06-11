package snake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Describes the directions of movement of the snake and suggests methods for performing these movements,
 * and also associates the direction of movement with key codes and images of the snake's head
 */
public enum Direction {
    LEFT (KeyEvent.VK_LEFT, new ImageIcon("src/kiri.png")) {
        @Override
        void move(int[] x, int[] y, int ballSize) {
            stepBody(x, y);
            x[0] -= ballSize;
        }
    },
    RIGHT (KeyEvent.VK_RIGHT, new ImageIcon("src/kanan.png")) {
        @Override
        void move(int[] x, int[] y, int ballSize) {
            stepBody(x, y);
            x[0] += ballSize;
        }
    },
    UP (KeyEvent.VK_UP, new ImageIcon("src/atas.png")) {
        @Override
        void move(int[] x, int[] y, int ballSize) {
            stepBody(x, y);
            y[0] -= ballSize;
        }
    },
    DOWN (KeyEvent.VK_DOWN, new ImageIcon("src/bawah.png")) {
        @Override
        void move(int[] x, int[] y, int ballSize) {
            stepBody(x, y);
            y[0] += ballSize;
        }
    };

    Direction(int key, ImageIcon head) {
        this.key = key;
        this.head = head;
    }

    // Key code associated with direction
    private final int key;
    // Head image associated with direction
    private final ImageIcon head;
    // A map that allows to convert the code of the pressed key into the direction of movement of the snake
    private static final Map<Integer, Direction> keyToEnum = Stream.of(values())
            .collect(Collectors.toMap(Direction::getKey, e->e));

    /**
     * Method to get code of the key associated with direction
     * @return Key code associated with direction
     */
    private int getKey() {
        return key;
    }

    /**
     * Returns different images of the snake's head depending on its direction.
     * @return Image of a snake's head
     */
    public Image getImage() {
        return head.getImage();
    }

    /**
     * Converts a key code to a direction
     * @param key Code of key pressed
     * @return The direction corresponding to the key code pressed, or an empty Optional if there is no such key
     */
    public static Optional<Direction> fromKey(int key) {
        return Optional.ofNullable(keyToEnum.get(key));
    }

    /**
     * Moves the snake one step.
     * @param x Array of X coordinates of snake parts. Must be the same length as the Y coordinate array.
     * @param y Array of Y coordinates of snake parts. Must be the same length as the X coordinates array.
     * @param ballSize the size of one segment of a snake
     */
    abstract void move(int[] x, int[] y, int ballSize);

    /**
     * Moves the snakes body one step.
     * @param x Array of X coordinates of snake parts. Must be the same length as the Y coordinate array.
     * @param y Array of Y coordinates of snake parts. Must be the same length as the X coordinates array.
     */
    private static void stepBody(int[] x, int[] y) {
        for (int l = x.length-1; l > 0; l--) {
            x[l] = x[(l - 1)];
            y[l] = y[(l - 1)];
        }
    }
}
