package com.selfdot.libs.minecraft.midi;

import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

import javax.sound.midi.*;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

import static javax.sound.midi.ShortMessage.NOTE_ON;

public class MidiPlayer implements Receiver {

    private static final Random RANDOM = new Random();

    private final Supplier<List<ServerPlayerEntity>> playerListSupplier;

    public MidiPlayer(Supplier<List<ServerPlayerEntity>> playerListSupplier) {
        this.playerListSupplier = playerListSupplier;
    }

    private static final int PERCUSSION_CHANNEL = 9;

    @Override
    public void send(MidiMessage message, long timeStamp) {
        if (!(message instanceof ShortMessage shortMessage)) return;
        if (shortMessage.getCommand() != NOTE_ON) return;
        int velocity = shortMessage.getData2();
        if (velocity == 0) return;
        int note = shortMessage.getData1();
        SoundEvent soundEvent;
        float pitch;
        if (shortMessage.getChannel() == PERCUSSION_CHANNEL) {
            pitch = 1f;
            switch (note) {
                case 36 -> soundEvent = SoundEvents.BLOCK_NOTE_BLOCK_BASEDRUM.value();
                case 38 -> soundEvent = SoundEvents.BLOCK_NOTE_BLOCK_SNARE.value();
                case 42 -> {
                    soundEvent = SoundEvents.BLOCK_NOTE_BLOCK_HAT.value();
                    pitch = 2f;
                }
                case 45 -> {
                    soundEvent = SoundEvents.BLOCK_NOTE_BLOCK_BASEDRUM.value();
                    pitch = 2f;
                }
                default -> {
                    return;
                }
            }
        } else {
            int offsetKey = note - 30;
            int inOctaveKey = offsetKey % 12;
            int range = offsetKey / 12;

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

        float finalPitch = pitch;
        playerListSupplier.get().forEach(player -> player.networkHandler.sendPacket(new PlaySoundS2CPacket(
            RegistryEntry.of(soundEvent),
            SoundCategory.RECORDS,
            player.getX(),
            player.getY() + 2,
            player.getZ(),
            velocity / 127f,
            finalPitch,
            RANDOM.nextLong()
        )));
    }

    @Override
    public void close() { }

}
