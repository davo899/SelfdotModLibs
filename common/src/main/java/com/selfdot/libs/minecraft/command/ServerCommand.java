package com.selfdot.libs.minecraft.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.selfdot.libs.minecraft.MinecraftMod;
import com.selfdot.libs.minecraft.permissions.Permission;
import com.selfdot.libs.minecraft.permissions.PermissionValidator;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public abstract class ServerCommand implements Command<ServerCommandSource> {

    protected abstract int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException;

    private final PermissionValidator permissionValidator;
    private final List<String> roots = new ArrayList<>();
    private Permission permission;
    @Setter
    private boolean requiresPlayer = false;

    protected ServerPlayerEntity player;

    public ServerCommand(@NotNull PermissionValidator permissionValidator, @NotNull String root) {
        this.permissionValidator = permissionValidator;
        roots.add(root);
    }

    public ServerCommand(@NotNull MinecraftMod mod, @NotNull String root) {
        this(mod.getPermissionValidator(), root);
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
        permissionValidator.register(permission);
    }

    protected boolean requirement(ServerCommandSource source) {
        return (permission == null || permissionValidator.hasPermission(source, permission)) &&
            (!requiresPlayer || source.isExecutedByPlayer());
    }

    protected LiteralArgumentBuilder<ServerCommandSource> node(LiteralArgumentBuilder<ServerCommandSource> root) {
        return root.executes(this);
    }

    public void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        roots.forEach(root -> dispatcher.register(node(
            LiteralArgumentBuilder.<ServerCommandSource>literal(root).requires(this::requirement)
        )));
    }

    @Override
    public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        try {
            if (requiresPlayer) {
                player = context.getSource().getPlayer();
                if (player == null) return 0;
            }
            return execute(context);
        } catch (CommandSyntaxException e) {
            throw e;
        } catch (Exception e) {
            log.error("An error occurred: ");
            e.printStackTrace(System.err);
        } catch (Throwable t) {
            log.error(String.valueOf(t));
            log.error(t.getMessage());
            log.error(Arrays.toString(t.getStackTrace()));
        }
        return 0;
    }

}
