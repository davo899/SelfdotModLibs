package com.selfdot.libs.v5.minecraft.midi.event;

@Deprecated(forRemoval = true)
public class MIDIEvent {

    public int time;
    public String type = "Empty";

    public MIDIEvent(int time) {
        this.time = time;
    }

}
