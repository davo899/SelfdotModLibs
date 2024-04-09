package com.selfdot.libs.minecraft.midi.event;

public class NoteOnEvent extends MIDIEvent {

    public int channel;
    public int key;
    public int velocity;

    public NoteOnEvent(int time, int channel, int key, int velocity) {
        super(time);
        this.type = "Note On";
        this.channel = channel;
        this.key = key;
        this.velocity = velocity;
    }

}
