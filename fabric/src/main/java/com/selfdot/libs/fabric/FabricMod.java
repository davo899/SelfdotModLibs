package com.selfdot.libs.fabric;

import com.selfdot.libs.minecraft.MinecraftMod;
import net.fabricmc.api.ModInitializer;

public abstract class FabricMod extends MinecraftMod implements ModInitializer {

    public FabricMod(String modId, boolean withReload) {
        super(modId, withReload);
        setPermissionValidator(new FabricPermissionValidator());
    }

    public FabricMod(String modId) {
        this(modId, true);
    }

}
