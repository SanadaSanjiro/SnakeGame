package snake.audio;

import java.io.File;

/**
 * Abstraction able to play midi files.
 */
public interface MidiPlayer {
    void playMidi(File midiFile);

    void stop();
}
