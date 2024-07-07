package com.selfdot.libs.forge;

import com.selfdot.libs.SelfdotModLibs;
import com.selfdot.libs.minecraft.task.TaskRunner;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;

@Mod(SelfdotModLibs.MOD_ID)
public class SelfdotModLibsForge {

    public SelfdotModLibsForge() { }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void event(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            TaskRunner.getInstance().onTick(ServerLifecycleHooks.getCurrentServer());
        }
    }

}
