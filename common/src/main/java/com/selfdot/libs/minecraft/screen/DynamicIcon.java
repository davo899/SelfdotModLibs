package com.selfdot.libs.minecraft.screen;

import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class DynamicIcon extends Icon {

    private final Function<Integer, ItemStack> itemStackSupplier;
    private int ticks = 0;

    public DynamicIcon(@NotNull Function<Integer, ItemStack> itemStackSupplier) {
        super(itemStackSupplier.apply(0));
        this.itemStackSupplier = itemStackSupplier;
    }

    public boolean tick() {
        ItemStack nextItem = itemStackSupplier.apply(ticks++);
        if (nextItem == null) return false;
        itemStack = nextItem;
        return true;
    }

}
