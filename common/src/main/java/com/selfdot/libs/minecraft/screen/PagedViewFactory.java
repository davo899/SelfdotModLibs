package com.selfdot.libs.minecraft.screen;

import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.elements.GuiElementBuilderInterface;
import eu.pb4.sgui.api.gui.SimpleGui;
import eu.pb4.sgui.api.gui.SimpleGuiBuilder;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.selfdot.libs.minecraft.screen.ScreenUtils.absolutePosition;
import static com.selfdot.libs.minecraft.screen.ScreenUtils.relativePosition;
import static net.minecraft.item.Items.ARROW;

public class PagedViewFactory<U> extends ViewFactory {

    private int page = 0;
    private final Supplier<List<U>> elementsSupplier;
    private final Function<U, GuiElementBuilderInterface<?>> iconFactory;
    private final Consumer<U> onElementClick;
    private final ServerPlayerEntity player;

    public PagedViewFactory(
        ScreenHandlerType<?> screenHandlerType,
        Consumer<SimpleGuiBuilder> create,
        ServerPlayerEntity player,
        Supplier<List<U>> elementsSupplier,
        Function<U, GuiElementBuilderInterface<?>> iconFactory,
        Consumer<U> onElementClick
    ) {
        super(screenHandlerType, create);
        this.player = player;
        this.elementsSupplier = elementsSupplier;
        this.iconFactory = iconFactory;
        this.onElementClick = onElementClick;
    }

    private void movePage(int offset, int size, int elementsPerPage) {
        page += offset;
        int pageCount = (size / elementsPerPage) + 1;
        while (page < 0) page += pageCount;
        while (page >= pageCount) page -= pageCount;
        super.open(player);
    }

    @Override
    public SimpleGui open(ServerPlayerEntity player) {
        page = 0;
        return super.open(player);
    }

    @Override
    protected void build(SimpleGuiBuilder guiBuilder) {
        int elementsPerRow = guiBuilder.getWidth();
        int elementsPerPage = elementsPerRow * (guiBuilder.getHeight() - 2);
        List<U> elements = elementsSupplier.get();
        for (int i = 0; i < Math.min(elementsPerPage, elements.size() - (page * elementsPerPage)); i++) {
            U element = elements.get((page * elementsPerPage) + i);
            guiBuilder.setSlot(
                absolutePosition(guiBuilder, i % elementsPerRow, 1 + (i / elementsPerRow)),
                iconFactory.apply(element).setCallback(() -> onElementClick.accept(element))
            );
        }
        guiBuilder.setSlot(
            relativePosition(guiBuilder, 0, 1),
            new GuiElementBuilder()
                .setItem(ARROW)
                .setName(Text.literal("Previous Page"))
                .setCallback(() -> movePage(-1, elements.size(), elementsPerPage))
        );
        guiBuilder.setSlot(
            relativePosition(guiBuilder, 1, 1),
            new GuiElementBuilder()
                .setItem(ARROW)
                .setName(Text.literal("Next Page"))
                .setCallback(() -> movePage(1, elements.size(), elementsPerPage))
        );
        create.accept(guiBuilder);
    }

}
