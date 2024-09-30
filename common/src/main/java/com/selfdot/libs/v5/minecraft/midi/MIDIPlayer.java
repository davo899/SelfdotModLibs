package com.selfdot.libs.v5.minecraft.midi;

import com.selfdot.libs.v5.minecraft.midi.event.MIDIEvent;
import com.selfdot.libs.v5.minecraft.midi.event.NoteOnEvent;
import com.selfdot.libs.v5.minecraft.midi.event.SetTempoEvent;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Deprecated(forRemoval = true)
public class MIDIPlayer {

    private final ServerPlayerEntity player;
	private MIDI midi;
	private List<Track> tracks = new ArrayList<>();
	private double timerInc = 80;
	
	public MIDIPlayer(ServerPlayerEntity player, MIDI midi) {
        this.player = player;
		this.midi = midi;
		
		for (List<MIDIEvent> track : midi.tracks) {
			Track trackObj = new Track(track);
			
			trackObj.timer += trackObj.events.get(trackObj.events.size() - 1).time;
			tracks.add(trackObj);
		}
	}
	
	public boolean tick(double timeFactor) {
		boolean ended = true;
        for (Track track : tracks) {
            while (track.timer <= 0) {
                if (track.events.size() == 0) break;

                MIDIEvent event = track.events.remove(track.events.size()-1);
                track.events.add(0, event);
                track.timer += track.events.get(track.events.size() - 1).time / timeFactor;

                if (event.type.equals("Note On")) {
                    NoteOnEvent noteOnEvent = (NoteOnEvent) event;

                    if (noteOnEvent.key >= 30) {
                        if (noteOnEvent.channel != 9) {
                            int offsetKey = noteOnEvent.key - 30;
                            int inOctaveKey = offsetKey % 12;
                            int range = Math.floorDiv(offsetKey, 12);

                            SoundEvent soundEvent;
                            if (range <= 0) soundEvent = SoundEvents.BLOCK_NOTE_BLOCK_BASS.value();
                            else if (range == 1) soundEvent = SoundEvents.BLOCK_NOTE_BLOCK_GUITAR.value();
                            else if (range == 2) {
                                soundEvent = SoundEvents.BLOCK_NOTE_BLOCK_GUITAR.value();
                                inOctaveKey += 12;
                            } else if (range == 3) {
                                soundEvent = SoundEvents.BLOCK_NOTE_BLOCK_BIT.value();
                                inOctaveKey += 12;
                            } else soundEvent = SoundEvents.BLOCK_NOTE_BLOCK_CHIME.value();

                            player.networkHandler.sendPacket(new PlaySoundS2CPacket(
                                RegistryEntry.of(soundEvent),
                                SoundCategory.RECORDS,
                                player.getX(),
                                player.getY() + 2,
                                player.getZ(),
                                (float) noteOnEvent.velocity / 127,
                                (float)Math.pow(2.0, ((double)inOctaveKey - 12.0) / 12.0),
                                player.getWorld().getRandom().nextLong()
                            ));

                        } else {
                            float pitch = 1;

                            SoundEvent soundEvent = null;
                            switch (noteOnEvent.key) {
                                case 36 -> soundEvent = SoundEvents.BLOCK_NOTE_BLOCK_BASEDRUM.value();
                                case 38 -> soundEvent = SoundEvents.BLOCK_NOTE_BLOCK_SNARE.value();
                                case 42 -> {
                                    soundEvent = SoundEvents.BLOCK_NOTE_BLOCK_HAT.value();
                                    pitch = 2;
                                }
                                case 45 -> {
                                    soundEvent = SoundEvents.BLOCK_NOTE_BLOCK_BASEDRUM.value();
                                    pitch = 2;
                                }
                            }

                            if (soundEvent != null) {
                                player.networkHandler.sendPacket(new PlaySoundS2CPacket(
                                    RegistryEntry.of(soundEvent),
                                    SoundCategory.RECORDS,
                                    player.getX(),
                                    player.getY() + 2,
                                    player.getZ(),
                                    (float) noteOnEvent.velocity / 127,
                                    pitch,
                                    player.getWorld().getRandom().nextLong()
                                ));
                            }
                        }
                    }

                } else if (event.type.equals("Set Tempo")) {
                    SetTempoEvent setTempoEvent = (SetTempoEvent) event;
                    this.timerInc = (50000d * this.midi.deltaTime) / setTempoEvent.tempo;
                }
            }
            if (track.events.size() > 0) {
                track.timer -= this.timerInc;
                ended = false;
            }
        }
		return ended;
	}

    public boolean tick() {
        return tick(1d);
    }

}
