package com.selfdot.libs.minecraft.midi;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.server.network.ServerPlayerEntity;

import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Supplier;

import static javax.sound.midi.Sequencer.LOOP_CONTINUOUSLY;

@Slf4j
public class MidiBank {

    @Getter
    private static final MidiBank instance = new MidiBank();

    private MidiBank() { }

    private final Map<String, Sequence> midis = new HashMap<>();

    public void load(Path path) {
        File file = path.toFile();
        if (!file.exists()) {
            log.error("File does not exist: {}", path);
            return;
        }
        Sequence sequence;
        try {
            sequence = MidiSystem.getSequence(file);
        } catch (InvalidMidiDataException e) {
            log.error("File was not valid MIDI: {}", path);
            return;
        } catch (IOException e) {
            log.error("Could not access {}: {}", path, e.getMessage());
            return;
        }
        String id = path.getFileName().toString();
        if (id.endsWith(".mid")) id = id.substring(0, id.length() - 4);
        midis.put(id, sequence);
    }

    public Sequencer tryPlaySong(String midi, boolean looped, Supplier<List<ServerPlayerEntity>> playerListSupplier) {
        if (!midis.containsKey(midi)) return null;
        try {
            Sequencer sequencer = MidiSystem.getSequencer(false);
            sequencer.open();
            sequencer.setSequence(midis.get(midi));
            if (looped) {
                sequencer.setLoopStartPoint(0);
                sequencer.setLoopEndPoint(-1);
                sequencer.setLoopCount(LOOP_CONTINUOUSLY);
            }
            sequencer.getTransmitter().setReceiver(new MidiPlayer(playerListSupplier));
            sequencer.start();
            return sequencer;

        } catch (MidiUnavailableException | InvalidMidiDataException e) {
            e.printStackTrace(System.err);
            return null;
        }
    }

    public boolean contains(String midiId) {
        return midis.containsKey(midiId);
    }

}
