package com.selfdot.libs.minecraft.screen;

import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;
import eu.pb4.sgui.api.gui.SimpleGuiBuilder;
import lombok.Setter;
import net.minecraft.item.Item;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import static com.selfdot.libs.minecraft.screen.ScreenUtils.relativePosition;
import static net.minecraft.item.Items.BARRIER;
import static net.minecraft.util.Formatting.RED;

public abstract class Menu {

    protected final ServerPlayerEntity player;
    private final Item backgroundItem;
    private SimpleGui gui;

    protected ViewFactory index = new ViewFactory(ScreenHandlerType.GENERIC_9X3, guiBuilder -> { });

    protected abstract void buildViewFactories();

    public Menu(ServerPlayerEntity player, Item backgroundItem) {
        this.player = player;
        this.backgroundItem = backgroundItem;
        buildViewFactories();
        openView(index);
    }

    public Menu(ServerPlayerEntity player) {
        this(player, null);
    }

    protected void openView(ViewFactory viewFactory, boolean shouldReset) {
        if (shouldReset) viewFactory.reset();
        viewFactory.setBackgroundItem(backgroundItem);
        gui = viewFactory.open(player);
    }

    protected void openView(ViewFactory viewFactory) {
        openView(viewFactory, true);
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
                .setCallback(() -> openView(viewFactory, false))
                .build()
        );
    }

}
