package com.selfdot.libs.minecraft.midi.event;

public class MIDIEvent {

    public int time;
    public String type = "Empty";

    public MIDIEvent(int time) {
        this.time = time;
    }

}
