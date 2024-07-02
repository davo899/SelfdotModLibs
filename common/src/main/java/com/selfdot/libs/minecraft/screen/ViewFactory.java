package com.selfdot.libs.minecraft.screen;

import eu.pb4.sgui.api.gui.SimpleGuiBuilder;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.function.Consumer;

public class ViewFactory {

    private final ScreenHandlerType<?> screenHandlerType;
    protected final Consumer<SimpleGuiBuilder> create;

    public ViewFactory(ScreenHandlerType<?> screenHandlerType, Consumer<SimpleGuiBuilder> create) {
        this.screenHandlerType = screenHandlerType;
        this.create = create;
    }

    protected void build(SimpleGuiBuilder guiBuilder) {
        create.accept(guiBuilder);
    }

    public void open(ServerPlayerEntity player) {
        SimpleGuiBuilder guiBuilder = new SimpleGuiBuilder(screenHandlerType, false);
        build(guiBuilder);
        guiBuilder.build(player).open();
    }

}
