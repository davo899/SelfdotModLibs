package com.selfdot.libs.minecraft.midi;

import lombok.Setter;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;

import static javax.sound.midi.ShortMessage.NOTE_ON;

public class MidiPlayer {

    private static final int PERCUSSION_CHANNEL = 9;

    private long currentTickTime = 0;
    private double ticksPerMillisecond;
    private long microsecondsPerQuarterNote = 500000;
    @Setter private double speedMultiplier = 1;

    private final Midi midi;
    private final ServerPlayerEntity player;

    public MidiPlayer(Midi midi, ServerPlayerEntity player) {
        this.midi = midi;
        this.player = player;
        ticksPerMillisecond = midi.ticksPerQuarterNote() / (microsecondsPerQuarterNote / 1000.0);
    }

    public void advance(long milliseconds) {
        currentTickTime += (long) (milliseconds * speedMultiplier);
        while (!midi.events().isEmpty() && midi.events().peek().getTick() < currentTickTime * ticksPerMillisecond) {
            MidiEvent event = midi.events().poll();
            if (event == null) continue;
            MidiMessage message = event.getMessage();
            if (message instanceof ShortMessage shortMessage) {
                if (shortMessage.getCommand() == NOTE_ON) {
                    int velocity = shortMessage.getData2();
                    if (velocity == 0) return;
                    int note = shortMessage.getData1();
                    SoundEvent soundEvent;
                    float pitch;
                    if (shortMessage.getChannel() == PERCUSSION_CHANNEL) {
                        pitch = 1;
                        switch (note) {
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
                            default -> {
                                continue;
                            }
                        }

                    } else {
                        int offsetKey = note - 30;
                        int inOctaveKey = offsetKey % 12;
                        int range = Math.floorDiv(offsetKey, 12);
                        if (range <= 0) soundEvent = SoundEvents.BLOCK_NOTE_BLOCK_BASS.value();
                        else if (range == 1) soundEvent = SoundEvents.BLOCK_NOTE_BLOCK_GUITAR.value();
                        else if (range == 2) {
                            soundEvent = SoundEvents.BLOCK_NOTE_BLOCK_GUITAR.value();
                            inOctaveKey += 12;
                        } else if (range == 3) {
                            soundEvent = SoundEvents.BLOCK_NOTE_BLOCK_BIT.value();
                            inOctaveKey += 12;
                        } else soundEvent = SoundEvents.BLOCK_NOTE_BLOCK_CHIME.value();
                        pitch = (float) Math.pow(2f, (inOctaveKey - 12f) / 12f);
                    }
                    player.networkHandler.sendPacket(new PlaySoundS2CPacket(
                        RegistryEntry.of(soundEvent),
                        SoundCategory.RECORDS,
                        player.getX(),
                        player.getY() + 2,
                        player.getZ(),
                        velocity / 127f,
                        pitch,
                        player.getWorld().getRandom().nextLong()
                    ));
                }

            } else if (message instanceof MetaMessage metaMessage) {
                if (metaMessage.getType() == 0x51) {
                    byte[] data = metaMessage.getData();
                    microsecondsPerQuarterNote = ((data[0] & 0xFF) << 16)
                        | ((data[1] & 0xFF) << 8)
                        | (data[2] & 0xFF);
                    ticksPerMillisecond = midi.ticksPerQuarterNote() / (microsecondsPerQuarterNote / 1000.0);
                }
            }
        }
    }

}