package snake.audio;

import snake.MidiPlayer;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import java.io.File;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class MidiAudioPlayer implements Runnable, MidiPlayer {
    private final static ExecutorService es = ExecutorServiceProvider.getExecutorService();
    private final static MidiPlayer player = new MidiAudioPlayer();
    private File midiFile = null;
    private boolean keepPlay = true;

    public static MidiPlayer getPlayer() {
        return player;
    }

    private MidiAudioPlayer() {
        es.execute(this);
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            if (Objects.nonNull(midiFile)) {
                try (Sequencer sequencer = MidiSystem.getSequencer();) {
                    if (sequencer == null) {
                        System.err.println("Sequencer is not supported");
                        System.exit(0);
                    }
                    // Открываем секвенсор
                    sequencer.open();
                    // Получаем MIDI-последовательность из файла
                    Sequence seq = MidiSystem.getSequence(midiFile);
                    // Направляем последовательность в секвенсор
                    sequencer.setSequence(seq);
                    long trackLength = sequencer.getMicrosecondLength();
                    // Начинаем проигрывание
                    sequencer.start();
                    // Здесь надо сделать задержку на время проигрывания,
                    while(trackLength > sequencer.getMicrosecondPosition() && keepPlay) {
                        TimeUnit.SECONDS.sleep(1);
                    }
                    // а затем остановить:
                    sequencer.stop();
                } catch(Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    @Override
    public void playMidi(File midiFile) {
        synchronized (this) {
            this.midiFile = midiFile;
        }
    }

    @Override
    public void stop() {
        synchronized (this) {
            keepPlay = false;
        }
    }
}
