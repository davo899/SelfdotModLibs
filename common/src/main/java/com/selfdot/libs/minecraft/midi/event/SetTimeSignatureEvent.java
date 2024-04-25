package com.selfdot.libs.minecraft.midi.event;

import java.util.ArrayList;
import java.util.List;

public class SetTimeSignatureEvent extends MIDIEvent {

    public List<Integer> ts = new ArrayList<>();
    public int clocksPerTick;
    public int notesPerClocks;

    public SetTimeSignatureEvent(int time, int timeSig1, int timeSig2, int clocksPerTick, int notesPerClocks) {
        super(time);
        ts.add(timeSig1);
        ts.add(timeSig2);
        this.clocksPerTick = clocksPerTick;
        this.notesPerClocks = notesPerClocks;
    }

}
