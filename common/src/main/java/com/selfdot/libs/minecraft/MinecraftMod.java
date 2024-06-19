package com.selfdot.libs.minecraft;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.selfdot.libs.minecraft.permissions.Permission;
import com.selfdot.libs.minecraft.permissions.PermissionLevel;
import com.selfdot.libs.minecraft.permissions.PermissionValidator;
import com.selfdot.libs.minecraft.screen.MenuHandler;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.event.events.common.TickEvent;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static com.selfdot.libs.minecraft.permissions.CommandRequirementBuilder.requirement;

public abstract class MinecraftMod {

    private final String modId;
    private final boolean withReload;
    private PermissionValidator permissionValidator;

    public MinecraftMod(String modId, boolean withReload) {
        this.modId = modId;
        this.withReload = withReload;
    }

    public MinecraftMod(String modId) {
        this(modId, true);
    }

    public PermissionValidator getPermissionValidator() {
        return permissionValidator;
    }

    public void setPermissionValidator(PermissionValidator permissionValidator) {
        this.permissionValidator = permissionValidator;
    }

    public void reload() { }

    public void onInitialize() {
        TickEvent.SERVER_PRE.register(server -> MenuHandler.tickOpenMenus());

        if (withReload) {
            Permission reloadPermission = new Permission(
                modId + ".reload", PermissionLevel.MULTIPLAYER_MANAGEMENT
            );
            CommandRegistrationEvent.EVENT.register((dispatcher, registryAccess, environment) ->
                dispatcher.register(LiteralArgumentBuilder.<ServerCommandSource>
                    literal("reload")
                    .requires(requirement(this).needsPermission(reloadPermission).build())
                    .then(LiteralArgumentBuilder.<ServerCommandSource>
                        literal(modId)
                        .executes(context -> {
                            reload();
                            context.getSource().sendMessage(Text.literal("Reloaded " + modId));
                            return Command.SINGLE_SUCCESS;
                        })
                    )
                )
            );
        }
    }

}
