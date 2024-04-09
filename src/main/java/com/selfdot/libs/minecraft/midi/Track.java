package com.selfdot.libs.minecraft.midi;

import com.selfdot.libs.minecraft.midi.event.MIDIEvent;

import java.util.ArrayList;
import java.util.List;

public class Track {

    public List<MIDIEvent> events = new ArrayList<>();
    public double timer = 0;

    public Track(List<MIDIEvent> events) {
        for (MIDIEvent event : events) this.events.add(0, event);
    }

}
