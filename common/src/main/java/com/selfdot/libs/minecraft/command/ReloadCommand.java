package com.selfdot.libs.minecraft.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.selfdot.libs.minecraft.MinecraftMod;
import com.selfdot.libs.minecraft.permissions.Permission;
import com.selfdot.libs.minecraft.permissions.PermissionLevel;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class ReloadCommand extends CaughtCommand {

    public ReloadCommand(MinecraftMod mod, CommandDispatcher<ServerCommandSource> dispatcher) {
        super(mod, dispatcher, "reload", new Permission(
            mod.getModId() + ".reload", PermissionLevel.MULTIPLAYER_MANAGEMENT
        ));
    }

    @Override
    protected LiteralArgumentBuilder<ServerCommandSource> node(LiteralArgumentBuilder<ServerCommandSource> root) {
        return root.then(LiteralArgumentBuilder.<ServerCommandSource>literal(mod.getModId()).executes(this::execute));
    }

    @Override
    protected int execute(CommandContext<ServerCommandSource> context) {
        mod.reload();
        context.getSource().sendMessage(Text.literal("Reloaded " + mod.getModId()));
        return SINGLE_SUCCESS;
    }

}
