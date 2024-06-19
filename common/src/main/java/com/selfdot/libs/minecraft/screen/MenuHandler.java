package com.selfdot.libs.minecraft.screen;

import lombok.extern.slf4j.Slf4j;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MenuHandler extends GenericContainerScreenHandler {

    private static final List<Menu<?>> openMenus = new ArrayList<>();

    public static void tickOpenMenus() {
        openMenus.forEach(Menu::tick);
    }

    private final Menu<?> menu;

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
        log.info("MENU OPENED");
        openMenus.add(menu);
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
        setCursorStack(ItemStack.EMPTY);
        setPreviousCursorStack(ItemStack.EMPTY);
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        if (player instanceof ServerPlayerEntity) {
            log.info("MENU CLOSED");
            openMenus.remove(menu);
        }
    }

}
