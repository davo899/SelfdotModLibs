package com.selfdot.libs.minecraft;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

@Getter @Setter
public class ServerPosition {

    @Expose private int x;
    @Expose private int y;
    @Expose private int z;
    @Expose private Identifier dimension;

    public ServerPosition(int x, int y, int z, Identifier dimension) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimension = dimension;
    }

    public ServerPosition() { }

    @Deprecated(forRemoval = true)
    public int x() {
        return x;
    }

    @Deprecated(forRemoval = true)
    public int y() {
        return y;
    }

    @Deprecated(forRemoval = true)
    public int z() {
        return z;
    }

    @Deprecated(forRemoval = true)
    public Identifier dimension() {
        return dimension;
    }

    public ServerWorld world(MinecraftServer server) {
        if (server == null) return null;
        for (ServerWorld world : server.getWorlds()) {
            if (world.getRegistryKey().getValue().equals(dimension)) return world;
        }
        return null;
    }

    public boolean teleportPlayer(ServerPlayerEntity player, float yaw, float pitch) {
        ServerWorld world = world(player.getServer());
        if (world == null) return false;
        player.teleport(world, x, y, z, yaw, pitch);
        return true;
    }

    public boolean teleportPlayer(ServerPlayerEntity player) {
        return teleportPlayer(player, 0, 0);
    }

}
