package snake.audio;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import java.io.File;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Plays MIDI files in its own stream and provides simple control methods.
 * By article from phys.bspu.by site
 */
public class MidiAudioPlayer implements Runnable, MidiPlayer {
    private final static ExecutorService es = ExecutorServiceProvider.getExecutorService();
    private final static MidiPlayer player = new MidiAudioPlayer();
    private File midiFile = null;
    private boolean keepPlay;
    private final int delay=200; // interval between conditions checks in milliseconds

    // Provides a singleton with a MIDI player implementation
    public static MidiPlayer getPlayer() {
        return player;
    }

    private MidiAudioPlayer() {
        es.execute(this);
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            startSequence();
        }
    }

    /**
     * Plays a MIDI sequence as long as the keepPlay variable is true.
     */
    private void startSequence() {
        if (Objects.nonNull(midiFile)) {
            try (Sequencer sequencer = MidiSystem.getSequencer();) {
                if (sequencer == null) {
                    System.err.println("Sequencer is not supported");
                    System.exit(0);
                }
                sequencer.open();
                Sequence seq = MidiSystem.getSequence(midiFile); // gets a MIDI sequence from a file
                sequencer.setSequence(seq);                      // sends this sequence to a sequencer
                long trackLength = sequencer.getMicrosecondLength();    // length of melody
                sequencer.start();                               // starts play
                // Here you need to make a delay for the playback time
                while(trackLength > sequencer.getMicrosecondPosition() && keepPlay) {
                    TimeUnit.MILLISECONDS.sleep(delay);
                }
                // and then stop
                sequencer.stop();
            } catch(Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }

    /**
     * Starts play MIDI file
     * @param midiFile correct MIDI file should be provided
     */
    @Override
    public void playMidi(File midiFile) {
        synchronized (this) {
            keepPlay = true;
            this.midiFile = midiFile;
        }
    }

    /**
     * Stops MIDI playback. Makes some delay, defined in the variable with the same name,
     * to allow the player to stop correctly
     */
    @Override
    public void stop() {
        synchronized (this) {
            keepPlay = false;
            try {
                TimeUnit.MILLISECONDS.sleep(delay*2);
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
            }
        }
    }
}
