package com.selfdot.libs.minecraft.midi;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.server.network.ServerPlayerEntity;

import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

@Slf4j
public class MidiBank {

    @Getter
    private static final MidiBank instance = new MidiBank();

    private MidiBank() { }

    private final Map<String, Midi> midis = new HashMap<>();

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

        PriorityQueue<MidiEvent> eventPriorityQueue = new PriorityQueue<>(Comparator.comparingLong(MidiEvent::getTick));
        for (Track track : sequence.getTracks()) {
            for (int i = 0; i < track.size(); i++) {
                MidiEvent event = track.get(i);
                eventPriorityQueue.offer(event);
            }
        }
        midis.put(id, new Midi(eventPriorityQueue, sequence.getResolution()));
    }

    public MidiPlayer getNewPlayer(String midiId, ServerPlayerEntity player) {
        if (!midis.containsKey(midiId)) return null;
        Midi midi = midis.get(midiId);
        return new MidiPlayer(new Midi(new ArrayDeque<>(midi.events()), midi.ticksPerQuarterNote()), player);
    }

    public boolean contains(String midiId) {
        return midis.containsKey(midiId);
    }

}
