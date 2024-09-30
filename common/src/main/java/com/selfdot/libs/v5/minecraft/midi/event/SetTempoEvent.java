package com.selfdot.libs.v5.minecraft.midi.event;

@Deprecated(forRemoval = true)
public class SetTempoEvent extends MIDIEvent {

    public int tempo;

    public SetTempoEvent(int time, int tempo) {
        super(time);
        this.type = "Set Tempo";
        this.tempo = tempo;
    }

}
