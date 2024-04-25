package com.selfdot.libs.minecraft.midi.event;

public class SetTempoEvent extends MIDIEvent {

    public int tempo;

    public SetTempoEvent(int time, int tempo) {
        super(time);
        this.type = "Set Tempo";
        this.tempo = tempo;
    }

}
