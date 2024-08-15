package com.selfdot.libs.minecraft.midi;

import javax.sound.midi.MidiEvent;
import java.util.Queue;

public record Midi(Queue<MidiEvent> events, int ticksPerQuarterNote) { }
