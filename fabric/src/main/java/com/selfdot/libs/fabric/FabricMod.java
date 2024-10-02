package com.selfdot.libs.fabric;

import com.selfdot.libs.minecraft.MinecraftMod;
import com.selfdot.libs.minecraft.task.TaskRunner;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

public abstract class FabricMod extends MinecraftMod implements ModInitializer {

    public FabricMod(String modId, boolean withReload) {
        super(modId, withReload);
        setPermissionValidator(new FabricPermissionValidator());
    }

    public FabricMod(String modId) {
        this(modId, true);
    }

    @Override
    public void onInitialize() {
        SelfdotModLibsFabric.getInstance().initialise();
        super.onInitialize();
        CommandRegistrationCallback.EVENT.register(
            (dispatcher, registryAccess, environment) -> onRegisterCommands(dispatcher)
        );
    }

}
