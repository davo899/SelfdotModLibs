package com.selfdot.libs.minecraft.midi;

import com.selfdot.libs.minecraft.midi.event.MIDIEvent;

import java.util.ArrayList;
import java.util.List;

public class MIDI {

    public List<List<MIDIEvent>> tracks = new ArrayList<>();
    public int deltaTime;
    public int numOfTracks;
    public int format;
    public int fps;

}
