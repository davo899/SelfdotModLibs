package com.selfdot.libs.io;

import com.google.gson.GsonBuilder;
import com.selfdot.libs.minecraft.MinecraftMod;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.entity.player.PlayerEntity;

import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.selfdot.libs.io.JsonUtils.loadWithDefault;

@Slf4j
public class PlayerDataRegistry<T> extends JsonRegistry<T> {

    private final Supplier<T> defaultSupplier;

    public PlayerDataRegistry(Class<T> clazz, Supplier<T> defaultSupplier, String path) {
        super(clazz, path);
        this.defaultSupplier = defaultSupplier;
        this.gsonBuilder = new GsonBuilder()
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .excludeFieldsWithoutExposeAnnotation();
    }

    public PlayerDataRegistry(Class<T> clazz, Supplier<T> defaultSupplier, MinecraftMod mod) {
        this(clazz, defaultSupplier, "data/" + mod.getModId() + "/player-data/");
    }

    @Override
    public void load() { }

    @Override
    protected boolean validate(T item) {
        return super.validate(item) && item != null;
    }

    @Override
    protected boolean validateKey(String key) {
        try {
            UUID.fromString(key);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    public void getOrCreate(PlayerEntity player, Function<T, Boolean> madeDirty) {
        String playerId = player.getUuidAsString();
        T playerData = items.get(playerId);
        if (playerData == null) {
            T defaultData = defaultSupplier.get();
            playerData = loadWithDefault(
                gsonBuilder.create(), directoryPathString + playerId + ".json",
                defaultData.getClass(), defaultData
            );
            items.put(playerId, playerData);
        }
        if (madeDirty.apply(playerData)) save(playerId);
    }

}