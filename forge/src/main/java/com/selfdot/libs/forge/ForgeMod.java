package com.selfdot.libs.forge;

import com.selfdot.libs.minecraft.MinecraftMod;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public abstract class ForgeMod extends MinecraftMod {

    public ForgeMod(String modId) {
        super(modId);
        SelfdotModLibsForge.getInstance().initialise();
        setPermissionValidator(new ForgePermissionValidator());
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void event(RegisterCommandsEvent event) {
        onRegisterCommands(event.getDispatcher());
    }

}
