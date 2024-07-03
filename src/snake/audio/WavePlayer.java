package snake.audio;

import java.io.File;

/**
 * Abstraction able to play wave files.
 */

/**
 * waveFile should be proper sound file in .wav format
 */
public interface WavePlayer {
    void playWave(File waveFile);
}
