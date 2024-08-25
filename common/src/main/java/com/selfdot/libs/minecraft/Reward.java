package com.selfdot.libs.minecraft;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.List;
import java.util.function.Function;

import static com.selfdot.libs.minecraft.MinecraftUtils.colourize;
import static com.selfdot.libs.minecraft.command.CommandExecutionBuilder.execute;

public record Reward(
    String displayName,
    List<String> commandList,
    String receiverMessage,
    String globalMessage
) {

    public Reward(String displayName, List<String> commandList) {
        this(displayName, commandList, null, null);
    }

    public String fillName(String input) {
        return input.replace("%reward%", displayName);
    }

    public void give(ServerPlayerEntity player, Function<String, String> messageTransformer) {
        MinecraftServer server = player.getServer();
        if (server == null) return;
        commandList.forEach(command -> execute(command).withPlayer(player).as(server));
        if (receiverMessage != null) {
            player.sendMessage(Text.literal(colourize(fillName(
                messageTransformer == null ? receiverMessage : messageTransformer.apply(receiverMessage)
            ))));
        }
        if (globalMessage != null) {
            server.getPlayerManager().broadcast(Text.literal(colourize(fillName(
                messageTransformer == null ? globalMessage : messageTransformer.apply(globalMessage)
            ))), false);
        }
    }

    public void give(ServerPlayerEntity player) {
        give(player, null);
    }


    @Deprecated(forRemoval = true)
    public void giveAndTell(ServerPlayerEntity player, String message) {
        give(player);
        player.sendMessage(Text.literal(colourize(message.replace("%reward%", displayName))));
    }

}
