package com.selfdot.libs.minecraft.screen;

import eu.pb4.sgui.api.elements.GuiElementBuilderInterface;
import eu.pb4.sgui.api.elements.GuiElementInterface;

public abstract class ItemElementBuilder<T extends GuiElementBuilderInterface<T>> implements GuiElementBuilderInterface<T> {

    protected GuiElementInterface.ClickCallback callback;

    public abstract T withName(String name);

    public abstract T withLore(String lore);

    protected abstract T self();

    @Override
    public T setCallback(GuiElementInterface.ClickCallback callback) {
        this.callback = callback;
        return self();
    }

}
