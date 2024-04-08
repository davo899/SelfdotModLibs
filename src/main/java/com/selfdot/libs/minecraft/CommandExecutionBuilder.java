package com.selfdot.libs.minecraft;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

@Slf4j
public class CommandExecutionBuilder {

    private static final String PLAYER_PLACEHOLDER = "%player%";

    public static CommandExecutionBuilder execute(String command) {
        return new CommandExecutionBuilder(command);
    }

    private String command;

    private CommandExecutionBuilder(String command) {
        this.command = command;
    }

    public CommandExecutionBuilder withPlayer(PlayerEntity player) {
        this.command = command.replaceAll(PLAYER_PLACEHOLDER, player.getGameProfile().getName());
        return this;
    }

    private void executeAs(ServerCommandSource source) {
        try {
            source.getServer().getCommandManager().getDispatcher().execute(command, source);

        } catch (CommandSyntaxException e) {
            log.error("Could not run: " + command);
            log.error(e.getMessage());
            log.error(Arrays.toString(e.getStackTrace()));
        }
    }

    public void as(Entity entity) {
        executeAs(entity.getCommandSource());
    }

    public void as(@Nullable MinecraftServer server) {
        if (server == null) throw new IllegalStateException("Server reference is null");
        executeAs(server.getCommandSource());
    }

}
