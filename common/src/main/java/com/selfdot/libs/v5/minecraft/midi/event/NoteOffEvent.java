package com.selfdot.libs.v5.minecraft.midi.event;

@Deprecated(forRemoval = true)
public class NoteOffEvent extends NoteOnEvent {

    public NoteOffEvent(int time, int channel, int key, int velocity) {
        super(time, channel, key, velocity);
        this.type = "Note Off";
    }

}
