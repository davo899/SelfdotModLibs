package com.selfdot.libs.minecraft;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

public record ServerPosition(int x, int y, int z, Identifier dimension) {

    public boolean teleportPlayer(ServerPlayerEntity player, float yaw, float pitch) {
        if (player == null) return false;
        MinecraftServer server = player.getServer();
        if (server == null) return false;
        for (ServerWorld world : server.getWorlds()) {
            if (world.getRegistryKey().getValue().equals(dimension)) {
                player.teleport(world, x, y, z, 0, 0);
                return true;
            }
        }
        return false;
    }

    public boolean teleportPlayer(ServerPlayerEntity player) {
        return teleportPlayer(player, 0, 0);
    }

}
