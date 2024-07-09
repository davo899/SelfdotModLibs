package com.selfdot.libs.minecraft.screen;

import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;
import eu.pb4.sgui.api.gui.SimpleGuiBuilder;
import net.minecraft.item.Item;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import static com.selfdot.libs.minecraft.screen.ScreenUtils.relativePosition;
import static net.minecraft.item.Items.BARRIER;
import static net.minecraft.util.Formatting.RED;

public abstract class Menu {

    protected final ServerPlayerEntity player;
    private SimpleGui gui;

    protected ViewFactory index = new ViewFactory(ScreenHandlerType.GENERIC_9X3, guiBuilder -> { });

    protected abstract void buildViewFactories();

    public Menu(ServerPlayerEntity player) {
        this.player = player;
        buildViewFactories();
        openView(index);
    }

    protected void openView(ViewFactory viewFactory) {
        gui = viewFactory.open(player);
    }

    protected void close() {
        if (gui != null) gui.close();
    }

    protected void returnsTo(SimpleGuiBuilder guiBuilder, ViewFactory viewFactory) {
        guiBuilder.setSlot(
            relativePosition(guiBuilder, 1 / 2f, 1),
            new GuiElementBuilder()
                .setItem(BARRIER)
                .setName(Text.literal(RED + "Back"))
                .setCallback(() -> openView(viewFactory))
                .build()
        );
    }

    protected void fill(SimpleGuiBuilder guiBuilder, Item item) {
        for (int i = 0; i < guiBuilder.getSize(); i++) {
            if (guiBuilder.getSlot(i) == null) {
                guiBuilder.setSlot(
                    i, new GuiElementBuilder()
                        .setItem(item)
                        .setName(ScreenTexts.EMPTY)
                );
            }
        }
    }

}
