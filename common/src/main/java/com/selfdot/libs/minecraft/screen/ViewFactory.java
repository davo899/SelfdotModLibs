package com.selfdot.libs.minecraft.screen;

import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;
import eu.pb4.sgui.api.gui.SimpleGuiBuilder;
import lombok.Setter;
import net.minecraft.item.Item;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.function.Consumer;

public class ViewFactory {

    private final ScreenHandlerType<?> screenHandlerType;
    protected final Consumer<SimpleGuiBuilder> create;
    @Setter
    private Item backgroundItem;

    public ViewFactory(ScreenHandlerType<?> screenHandlerType, Consumer<SimpleGuiBuilder> create) {
        this.screenHandlerType = screenHandlerType;
        this.create = create;
    }

    protected void build(SimpleGuiBuilder guiBuilder) {
        create.accept(guiBuilder);
    }

    public SimpleGui open(SimpleGuiBuilder guiBuilder, ServerPlayerEntity player) {
        build(guiBuilder);
        if (backgroundItem != null) {
            for (int i = 0; i < guiBuilder.getSize(); i++) {
                if (guiBuilder.getSlot(i) == null) {
                    guiBuilder.setSlot(
                        i, new GuiElementBuilder().setItem(backgroundItem).setName(Text.literal(" "))
                    );
                }
            }
        }
        SimpleGui gui = guiBuilder.build(player);
        gui.open();
        return gui;
    }

    public SimpleGui open(ServerPlayerEntity player) {
        return open(new SimpleGuiBuilder(screenHandlerType, false), player);
    }

}
