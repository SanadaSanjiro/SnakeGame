package snake.audio;

import snake.WavePlayer;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ExecutorService;

/**
 * Implementation of the SoundPlayer interface. Starts a new thread to play a wave file.
 */

public class WaveAudioPlayer implements Runnable, WavePlayer {
    private final static ExecutorService es = ExecutorServiceProvider.getExecutorService();
    private final static WavePlayer player = new WaveAudioPlayer();
    private File waveFile = null;

    /**
     *  SoundPlayer factory
     * @return WaveAudioPlayer singleton
     */
    public static WavePlayer getPlayer() {
        return player;
    }

    private WaveAudioPlayer() {
        es.execute(this);
    }

    @Override
    public void run() {
        while(!Thread.interrupted()) {
            if (Objects.nonNull(waveFile)) {
                try (AudioInputStream ais = AudioSystem.getAudioInputStream(waveFile);
                    Clip clip = AudioSystem.getClip() ) {

                    // Loading audio stream into Clip
                    // May throw IOException and LineUnavailableException.
                    clip.open(ais);

                    clip.setFramePosition(0); // Setting pointer to the start
                    clip.start(); // Let's go!

                    // Waiting for the end of the clip
                    Thread.sleep(clip.getMicrosecondLength()/1000);
                    clip.stop(); // Stopping
                    waveFile = null; // Dropping filename
                } catch (IOException |
                         UnsupportedAudioFileException |
                         LineUnavailableException |
                         InterruptedException exc) {
                    System.err.println(exc.getMessage());
                }
            }
        }
    }

    /**
     * Set's sound file to play
     * @param waveFile Wave file to play
     */
    @Override
    public void playWave(File waveFile) {
        synchronized(this) {
            this.waveFile = waveFile;
        }
    }
}
