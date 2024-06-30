package com.selfdot.libs.minecraft.screen;

import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGuiBuilder;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import static com.selfdot.libs.minecraft.screen.ScreenUtils.relativePosition;
import static net.minecraft.item.Items.BARRIER;
import static net.minecraft.util.Formatting.RED;

public class Menu {

    private final ScreenHandlerType<?> screenHandlerType;
    protected final ServerPlayerEntity player;

    public Menu(ScreenHandlerType<?> screenHandlerType, ServerPlayerEntity player) {
        this.screenHandlerType = screenHandlerType;
        this.player = player;
    }

    private void openView(ViewFactory viewFactory) {
        SimpleGuiBuilder guiBuilder = new SimpleGuiBuilder(screenHandlerType, false);
        viewFactory.create(guiBuilder);
        guiBuilder.build(player).open();
    }

    private void returnsTo(SimpleGuiBuilder guiBuilder, ViewFactory viewFactory) {
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
