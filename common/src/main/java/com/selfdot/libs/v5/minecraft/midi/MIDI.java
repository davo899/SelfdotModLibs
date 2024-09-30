package com.selfdot.libs.v5.minecraft.midi;

import com.selfdot.libs.v5.minecraft.midi.event.MIDIEvent;

import java.util.ArrayList;
import java.util.List;

@Deprecated(forRemoval = true)
public class MIDI {

    public List<List<MIDIEvent>> tracks = new ArrayList<>();
    public int deltaTime;
    public int numOfTracks;
    public int format;
    public int fps;

}
