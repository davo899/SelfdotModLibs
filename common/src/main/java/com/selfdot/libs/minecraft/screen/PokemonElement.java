package com.selfdot.libs.minecraft.screen;

import eu.pb4.sgui.api.elements.GuiElementInterface;
import net.minecraft.item.ItemStack;

public class PokemonElement implements GuiElementInterface {

    private final ItemStackBuilder itemStack;
    private final ClickCallback callback;

    public PokemonElement(ItemStackBuilder itemStack, ClickCallback callback) {
        this.itemStack = itemStack;
        this.callback = callback;
    }

    @Override
    public ItemStack getItemStack() {
        return itemStack.build();
    }

    @Override
    public ClickCallback getGuiCallback() {
        return callback;
    }

}
