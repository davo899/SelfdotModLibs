package com.selfdot.libs.mixin;

import com.selfdot.libs.minecraft.event.EventHandler;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin {

    @Inject(method = "remove", at = @At("HEAD"))
    private void injectRemove(ServerPlayerEntity player, CallbackInfo ci) {
        EventHandler.getInstance().onPlayerLeave(player);
    }

}
