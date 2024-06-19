package com.selfdot.libs.minecraft.screen;

import net.minecraft.inventory.Inventory;

import java.util.function.Consumer;

public record Component<T extends Menu<T>>(int slot, Icon icon, Consumer<T> action, Consumer<T> navigation) {

    public void tick(Inventory inventory) {
        if (icon().tick()) inventory.setStack(slot, icon.getItemStack());
    }

}
