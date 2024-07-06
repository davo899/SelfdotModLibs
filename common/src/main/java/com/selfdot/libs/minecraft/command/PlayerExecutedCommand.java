package com.selfdot.libs.minecraft.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.selfdot.libs.minecraft.MinecraftMod;
import com.selfdot.libs.minecraft.permissions.Permission;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.List;

public abstract class PlayerExecutedCommand extends CaughtCommand {

    public PlayerExecutedCommand(
        MinecraftMod mod,
        CommandDispatcher<ServerCommandSource> dispatcher,
        List<String> roots,
        Permission permission
    ) {
        super(mod, dispatcher, roots, permission);
    }

    public PlayerExecutedCommand(
        MinecraftMod mod,
        CommandDispatcher<ServerCommandSource> dispatcher,
        List<String> roots
    ) {
        super(mod, dispatcher, roots);
    }

    public PlayerExecutedCommand(
        MinecraftMod mod,
        CommandDispatcher<ServerCommandSource> dispatcher,
        String root,
        Permission permission
    ) {
        super(mod, dispatcher, root, permission);
    }

    public PlayerExecutedCommand(MinecraftMod mod, CommandDispatcher<ServerCommandSource> dispatcher, String root) {
        super(mod, dispatcher, root);
    }

    @Override
    protected boolean requirement(ServerCommandSource source) {
        return super.requirement(source) && source.isExecutedByPlayer();
    }

    protected abstract int execute(
        CommandContext<ServerCommandSource> context, ServerPlayerEntity player
    ) throws CommandSyntaxException;

    @Override
    protected int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayer();
        if (player == null) return 0;
        return execute(context, player);
    }

}
