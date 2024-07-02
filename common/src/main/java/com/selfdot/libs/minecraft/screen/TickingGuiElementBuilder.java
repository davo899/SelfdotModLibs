package com.selfdot.libs.minecraft.screen;

import eu.pb4.sgui.api.elements.AnimatedGuiElement;
import eu.pb4.sgui.api.elements.AnimatedGuiElementBuilder;
import net.minecraft.item.ItemStack;

import java.util.function.Supplier;

public class TickingGuiElementBuilder extends AnimatedGuiElementBuilder {

    private Supplier<ItemStack> itemStackSupplier = () -> ItemStack.EMPTY;

    public TickingGuiElementBuilder withItemStackSupplier(Supplier<ItemStack> itemStackSupplier) {
        this.itemStackSupplier = itemStackSupplier;
        return this;
    }

    @Override
    public AnimatedGuiElement build() {
        return new TickingGuiElement(itemStackSupplier, interval, callback);
    }

}
