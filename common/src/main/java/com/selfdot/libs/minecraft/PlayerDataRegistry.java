package com.selfdot.libs.minecraft;

import com.google.gson.reflect.TypeToken;
import com.selfdot.libs.io.JsonRegistry;
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
    }

    public PlayerDataRegistry(Class<T> clazz, Supplier<T> defaultSupplier, MinecraftMod mod) {
        this(clazz, defaultSupplier, "data/" + mod.getModId() + "/player-data/");
    }

    @Override
    public void load() { }

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
            playerData = loadWithDefault(
                gson, directoryPathString + playerId + ".json", clazz, defaultSupplier.get()
            );
            items.put(playerId, playerData);
        }
        if (madeDirty.apply(playerData)) save(playerId);
    }

}
