package com.selfdot.libs.minecraft.screen;

import com.selfdot.libs.minecraft.task.TaskRunner;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandlerSyncHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.*;

@Slf4j
public class MenuHandler extends GenericContainerScreenHandler {

    private static final List<MenuHandler> openMenus = new ArrayList<>();

    public static void tickOpenMenus() {
        openMenus.forEach(MenuHandler::tick);
    }

    private final Menu<?> menu;
    private final PlayerInventory playerInventory;
    private final List<ItemStack> playerItems = new ArrayList<>();

    public MenuHandler(
        ScreenHandlerType<?> type,
        int syncId,
        PlayerInventory playerInventory,
        Inventory inventory,
        int rows,
        Menu<?> menu
    ) {
        super(type, syncId, playerInventory, inventory, rows);
        this.menu = menu;
        this.playerInventory = playerInventory;
        openMenus.add(this);
    }

    private void tick() {
        menu.tick();

        playerItems.clear();
        for (int i = 0; i < playerInventory.size(); i++) {
            playerItems.add(playerInventory.getStack(i));
        }
    }

    @Override
    public boolean canInsertIntoSlot(Slot slot) {
        return false;
    }

    @Override
    protected void dropInventory(PlayerEntity player, Inventory inventory) { }

    @Override
    public void onSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player) {
        menu.onSlotClick(slotIndex);
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        if (player instanceof ServerPlayerEntity) {
            openMenus.remove(this);
            TaskRunner.getInstance().runLater(() -> {
                for (int i = 0; i < playerInventory.size(); i++) {
                    playerInventory.setStack(i, playerItems.get(i));
                }
            }, 1);
        }
    }

}
