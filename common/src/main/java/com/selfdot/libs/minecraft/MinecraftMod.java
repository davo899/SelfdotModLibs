package com.selfdot.libs.minecraft;

import com.mojang.brigadier.CommandDispatcher;
import com.selfdot.libs.minecraft.command.ReloadCommand;
import com.selfdot.libs.minecraft.permissions.PermissionValidator;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.command.ServerCommandSource;

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

    public void onInitialize() { }

    public void onRegisterCommands(CommandDispatcher<ServerCommandSource> dispatcher) {
        if (withReload) new ReloadCommand(this, dispatcher);
    }

}
