package com.selfdot.libs.minecraft.screen;

import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGuiBuilder;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import static com.selfdot.libs.minecraft.screen.ScreenUtils.relativePosition;
import static net.minecraft.item.Items.BARRIER;
import static net.minecraft.util.Formatting.RED;

public abstract class Menu {

    protected final ServerPlayerEntity player;

    protected ViewFactory index = new ViewFactory(ScreenHandlerType.GENERIC_9X3, guiBuilder -> { });

    protected abstract void buildViewFactories();

    public Menu(ServerPlayerEntity player) {
        this.player = player;
        buildViewFactories();
        openView(index);
    }

    protected void openView(ViewFactory viewFactory) {
        viewFactory.open(player);
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

}