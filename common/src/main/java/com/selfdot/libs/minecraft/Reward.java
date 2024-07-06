package com.selfdot.libs.minecraft;

import net.minecraft.server.network.ServerPlayerEntity;

import java.util.List;

import static com.selfdot.libs.minecraft.command.CommandExecutionBuilder.execute;

public record Reward(String displayName, List<String> commandList) {

    public void give(ServerPlayerEntity player) {
        commandList.forEach(command -> execute(command).withPlayer(player).as(player.getServer()));
    }

}
