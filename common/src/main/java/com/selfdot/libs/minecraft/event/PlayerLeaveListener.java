package com.selfdot.libs.minecraft.event;

import net.minecraft.server.network.ServerPlayerEntity;

public interface PlayerLeaveListener {

    void onPlayerLeave(ServerPlayerEntity player);

}
