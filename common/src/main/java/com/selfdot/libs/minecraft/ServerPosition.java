package com.selfdot.libs.minecraft;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

public record ServerPosition(int x, int y, int z, Identifier dimension) {

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
