package com.selfdot.libs.minecraft;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.List;

import static com.selfdot.libs.minecraft.MinecraftUtils.colourize;
import static com.selfdot.libs.minecraft.command.CommandExecutionBuilder.execute;

public record Reward(String displayName, List<String> commandList) {

    public void give(ServerPlayerEntity player) {
        commandList.forEach(command -> execute(command).withPlayer(player).as(player.getServer()));
    }

    public void giveAndTell(ServerPlayerEntity player, String message) {
        give(player);
        player.sendMessage(Text.literal(colourize(message.replace("%reward%", displayName))));
    }

}
