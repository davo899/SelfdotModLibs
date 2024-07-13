package com.selfdot.libs.minecraft.event;

import lombok.Getter;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.List;

public class EventHandler {

    private EventHandler() { }

    @Getter
    private static final EventHandler instance = new EventHandler();

    private final List<PlayerLeaveListener> playerLeaveListeners = new ArrayList<>();

    public void addPlayerLeaveListener(PlayerLeaveListener listener) {
        playerLeaveListeners.add(listener);
    }

    public void onPlayerLeave(ServerPlayerEntity player) {
        playerLeaveListeners.forEach(playerLeaveListener -> playerLeaveListener.onPlayerLeave(player));
    }

}
