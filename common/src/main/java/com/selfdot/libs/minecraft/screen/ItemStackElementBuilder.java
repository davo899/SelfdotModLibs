package com.selfdot.libs.minecraft.screen;

import eu.pb4.sgui.api.elements.GuiElement;
import eu.pb4.sgui.api.elements.GuiElementInterface;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.item.Item;

import static com.selfdot.libs.minecraft.screen.ItemStackBuilder.itemStack;

@Slf4j
public class ItemStackElementBuilder extends ItemElementBuilder<ItemStackElementBuilder> {

    protected ItemStackBuilder itemStackBuilder;

    public static ItemStackElementBuilder item(Item item) {
        ItemStackElementBuilder itemStackElementBuilder = new ItemStackElementBuilder();
        itemStackElementBuilder.itemStackBuilder = itemStack(item);
        return itemStackElementBuilder;
    }

    public static ItemStackElementBuilder skull(String skull) {
        ItemStackElementBuilder itemStackElementBuilder = new ItemStackElementBuilder();
        itemStackElementBuilder.itemStackBuilder = ItemStackBuilder.skull(skull);
        return itemStackElementBuilder;
    }

    @Override
    public ItemStackElementBuilder withName(String name) {
        itemStackBuilder.withName(name);
        return this;
    }

    @Override
    public ItemStackElementBuilder withLore(String lore) {
        itemStackBuilder.withLore(lore);
        return this;
    }

    @Override
    protected ItemStackElementBuilder self() {
        return this;
    }

    @Override
    public GuiElementInterface build() {
        return new GuiElement(itemStackBuilder.build(), callback);
    }

}
