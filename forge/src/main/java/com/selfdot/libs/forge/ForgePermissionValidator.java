package com.selfdot.libs.forge;

import com.selfdot.libs.minecraft.permissions.Permission;
import com.selfdot.libs.minecraft.permissions.PermissionValidator;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.server.permission.PermissionAPI;
import net.minecraftforge.server.permission.events.PermissionGatherEvent;
import net.minecraftforge.server.permission.nodes.PermissionNode;
import net.minecraftforge.server.permission.nodes.PermissionTypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ForgePermissionValidator implements PermissionValidator {

    private final List<Permission> permissions = new ArrayList<>();
    private final Map<Identifier, PermissionNode<Boolean>> nodes = new HashMap<>();

    public ForgePermissionValidator() {
        MinecraftForge.EVENT_BUS.addListener(this::onPermissionGather);
    }

    @Override
    public void register(Permission permission) {
        permissions.add(permission);
    }

    private void onPermissionGather(PermissionGatherEvent.Nodes event) {
        permissions.stream().map(permission -> {
            PermissionNode<Boolean> node = new PermissionNode<>(
                permission.getIdentifier(),
                PermissionTypes.BOOLEAN,
                (player, a, b) -> {
                    if (player == null) return false;
                    return player.hasPermissionLevel(permission.level().ordinal());
                }
            );
            nodes.put(permission.getIdentifier(), node);
            return node;
        }).forEach(event::addNodes);
    }

    @Override
    public boolean hasPermission(CommandSource source, Permission permission) {
        if (
            !nodes.containsKey(permission.getIdentifier()) ||
            !(source instanceof ServerCommandSource serverCommandSource) ||
            serverCommandSource.getPlayer() == null
        ) {
            return source.hasPermissionLevel(permission.level().ordinal());
        }
        return PermissionAPI.getPermission(serverCommandSource.getPlayer(), this.nodes.get(permission.getIdentifier()));
    }

}
