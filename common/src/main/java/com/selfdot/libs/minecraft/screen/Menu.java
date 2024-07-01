package com.selfdot.libs.minecraft.screen;

import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.elements.GuiElementBuilderInterface;
import eu.pb4.sgui.api.gui.SimpleGuiBuilder;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.selfdot.libs.minecraft.screen.ScreenUtils.relativePosition;
import static net.minecraft.item.Items.BARRIER;
import static net.minecraft.util.Formatting.RED;

public abstract class Menu {

    private final ScreenHandlerType<?> screenHandlerType;
    protected final ServerPlayerEntity player;

    protected ViewFactory index;

    protected abstract void buildViewFactories();

    public Menu(ScreenHandlerType<?> screenHandlerType, ServerPlayerEntity player) {
        this.screenHandlerType = screenHandlerType;
        this.player = player;
        buildViewFactories();
        openView(index);
    }

    protected <U> PagedViewFactory<U> pagedViewFactory(
        Supplier<List<U>> elementsSupplier,
        Function<U, GuiElementBuilderInterface<?>> iconFactory,
        Consumer<U> onElementClick,
        ViewFactory viewFactory
    ) {
        return new PagedViewFactory<>(this::openView, elementsSupplier, iconFactory, onElementClick, viewFactory);
    }

    protected void openView(ViewFactory viewFactory) {
        SimpleGuiBuilder guiBuilder = new SimpleGuiBuilder(screenHandlerType, false);
        viewFactory.create(guiBuilder);
        guiBuilder.build(player).open();
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
