package com.selfdot.libs.minecraft.midi.event;

public class NoteOffEvent extends NoteOnEvent {

    public NoteOffEvent(int time, int channel, int key, int velocity) {
        super(time, channel, key, velocity);
        this.type = "Note Off";
    }

}
