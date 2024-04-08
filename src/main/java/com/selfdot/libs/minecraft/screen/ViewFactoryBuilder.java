package com.selfdot.libs.minecraft.screen;

import net.minecraft.item.Items;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ViewFactoryBuilder<T extends Menu<T>> {

    private final List<Component<T>> components = new ArrayList<>();
    private final List<Supplier<List<Component<T>>>> componentFactories = new ArrayList<>();
    private String returnsTo = "";


    public ViewFactoryBuilder<T> withComponents(Supplier<List<Component<T>>> componentFactory) {
        componentFactories.add(componentFactory);
        return this;
    }

    public ViewFactoryBuilder<T> withComponent(Supplier<Component<T>> componentFactory) {
        return withComponents(() -> List.of(componentFactory.get()));
    }

    public ViewFactoryBuilder<T> withComponent(Component<T> component) {
        components.add(component);
        return this;
    }

    public ViewFactoryBuilder<T> returnsTo(String returnsTo) {
        this.returnsTo = returnsTo;
        return this;
    }

    private static final int ELEMENTS_PER_ROW = 7;
    private static final int ELEMENTS_PER_PAGE = ELEMENTS_PER_ROW * 3;
    public <U> ViewFactory<T> paged9x6(
        Supplier<List<U>> elementsSupplier,
        Function<U, ItemStackBuilder> iconFactory,
        BiConsumer<T, U> onElementClick
    ) {
        return menu -> {
            List<U> elements = elementsSupplier.get();
            menu.setElementsPerPage(ELEMENTS_PER_PAGE);
            for (
                int i = 0; i < Math.min(ELEMENTS_PER_PAGE, elements.size() - (menu.getPage() * ELEMENTS_PER_PAGE)); i++
            ) {
                U element = elements.get((menu.getPage() * ELEMENTS_PER_PAGE) + i);
                components.add(
                    new ComponentBuilder<T>(
                        (i % ELEMENTS_PER_ROW) + 1, (i / ELEMENTS_PER_ROW) + 1, iconFactory.apply(element)
                    ).withAction(menu_ -> onElementClick.accept(menu_, element)).build()
                );
            }
            return build().create(menu);
        };
    }

    public ViewFactory<T> build() {
        return menu -> {
            if (!returnsTo.isEmpty()) {
                components.add(
                    new ComponentBuilder<T>(
                        menu.columns() / 2, menu.rows() - (menu.isBordered() ? 2 : 1), Items.BARRIER
                    ).withName("Back").navigatesTo(returnsTo).build()
                );
            }
            componentFactories.forEach(componentSupplier -> components.addAll(componentSupplier.get()));
            return new View<>(components);
        };
    }

}
