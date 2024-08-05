package com.selfdot.libs.minecraft.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.selfdot.libs.minecraft.MinecraftMod;
import com.selfdot.libs.minecraft.permissions.Permission;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.server.command.ServerCommandSource;

import java.util.Arrays;
import java.util.List;

@Slf4j
public abstract class CaughtCommand implements Command<ServerCommandSource> {

    protected final MinecraftMod mod;
    private final Permission permission;

    public CaughtCommand(
        MinecraftMod mod,
        CommandDispatcher<ServerCommandSource> dispatcher,
        List<String> roots,
        Permission permission
    ) {
        this.mod = mod;
        this.permission = permission;
        roots.forEach(root -> dispatcher.register(node(
            LiteralArgumentBuilder.<ServerCommandSource>literal(root).requires(this::requirement)
        )));
        mod.getPermissionValidator().register(permission);
    }

    public CaughtCommand(MinecraftMod mod, CommandDispatcher<ServerCommandSource> dispatcher, List<String> roots) {
        this(mod, dispatcher, roots, null);
    }

    public CaughtCommand(
        MinecraftMod mod,
        CommandDispatcher<ServerCommandSource> dispatcher,
        String root,
        Permission permission
    ) {
        this(mod, dispatcher, List.of(root), permission);
    }

    public CaughtCommand(MinecraftMod mod, CommandDispatcher<ServerCommandSource> dispatcher, String root) {
        this(mod, dispatcher, root, null);
    }

    protected boolean requirement(ServerCommandSource source) {
        return permission == null || mod.getPermissionValidator().hasPermission(source, permission);
    }

    protected abstract LiteralArgumentBuilder<ServerCommandSource> node(
        LiteralArgumentBuilder<ServerCommandSource> root
    );

    protected abstract int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException;

    @Override
    public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        try {
            return execute(context);
        } catch (CommandSyntaxException e) {
            throw e;
        } catch (Exception e) {
            log.error("An error occurred: ");
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        } catch (Throwable t) {
            log.error(String.valueOf(t));
            log.error(t.getMessage());
            log.error(Arrays.toString(t.getStackTrace()));
        }
        return 0;
    }

}
