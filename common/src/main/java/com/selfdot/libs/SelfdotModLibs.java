package com.selfdot.libs;

import com.mojang.brigadier.CommandDispatcher;
import com.selfdot.libs.cobblemon.command.PunchingBagCommand;
import com.selfdot.libs.minecraft.permissions.PermissionValidator;
import lombok.Getter;
import net.minecraft.server.command.ServerCommandSource;
import org.jetbrains.annotations.NotNull;

public class SelfdotModLibs {

    public static final String MOD_ID = "selfdotmodlibs";

    @Getter
    private static final SelfdotModLibs instance = new SelfdotModLibs();

    private SelfdotModLibs() { }

    private boolean commandsRegistered = false;

    public void registerCommands(@NotNull PermissionValidator permissionValidator, CommandDispatcher<ServerCommandSource> dispatcher) {
        if (commandsRegistered) return;
        commandsRegistered = true;
        try {
            Class.forName("com.cobblemon.mod.common.Cobblemon");
            new PunchingBagCommand(permissionValidator).register(dispatcher);
        } catch (ClassNotFoundException ignored) { }
    }

}
