package com.selfdot.libs.minecraft;

import com.selfdot.libs.minecraft.command.ReloadCommand;
import com.selfdot.libs.minecraft.permissions.PermissionValidator;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import lombok.Getter;
import lombok.Setter;

public abstract class MinecraftMod {

    @Getter
    private final String modId;
    private final boolean withReload;
    @Getter @Setter
    private PermissionValidator permissionValidator;

    public MinecraftMod(String modId, boolean withReload) {
        this.modId = modId;
        this.withReload = withReload;
    }

    public MinecraftMod(String modId) {
        this(modId, true);
    }

    public void reload() { }

    public void onInitialize() {
        if (withReload) {
            CommandRegistrationEvent.EVENT.register(
                (dispatcher, registryAccess, environment) -> new ReloadCommand(this, dispatcher)
            );
        }
    }

}
