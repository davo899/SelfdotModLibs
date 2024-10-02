package com.selfdot.libs.fabric;

import com.selfdot.libs.SelfdotModLibs;
import com.selfdot.libs.minecraft.task.TaskRunner;
import lombok.Getter;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

public class SelfdotModLibsFabric extends SelfdotModLibs {

    @Getter
    private static final SelfdotModLibsFabric instance = new SelfdotModLibsFabric();

    private SelfdotModLibsFabric() {
        super();
        SelfdotModLibs.instance = this;
        ServerTickEvents.START_SERVER_TICK.register(server -> TaskRunner.getInstance().onTick(server));
    }

    public void initialise() { }

}
