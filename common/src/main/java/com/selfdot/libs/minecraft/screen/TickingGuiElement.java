package com.selfdot.libs.minecraft.screen;

import eu.pb4.sgui.api.elements.AnimatedGuiElement;
import eu.pb4.sgui.api.gui.GuiInterface;
import net.minecraft.item.ItemStack;

import java.util.function.Supplier;

public class TickingGuiElement extends AnimatedGuiElement {

    private final Supplier<ItemStack> itemStackSupplier;
    private ItemStack itemStack;

    public TickingGuiElement(Supplier<ItemStack> itemStackSupplier, int interval, ClickCallback callback) {
        super(new ItemStack[]{}, interval, false, callback);
        this.itemStackSupplier = itemStackSupplier;
        itemStack = itemStackSupplier.get();
    }

    @Override
    public ItemStack getItemStack() {
        return itemStack;
    }

    @Override
    public ItemStack getItemStackForDisplay(GuiInterface gui) {
        if (++tick >= changeEvery) {
            tick = 0;
            itemStack = itemStackSupplier.get();
        }
        return itemStack.copy();
    }

}
