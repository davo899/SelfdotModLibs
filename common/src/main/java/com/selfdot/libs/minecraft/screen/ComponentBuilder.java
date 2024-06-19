package com.selfdot.libs.minecraft.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;

import static com.selfdot.libs.minecraft.screen.ItemStackBuilder.itemStack;
import static com.selfdot.libs.minecraft.screen.ItemStackBuilder.skullOf;

public class ComponentBuilder<T extends Menu<T>> extends AbstractComponentBuilder<T, ComponentBuilder<T>> {

    private final ItemStackBuilder icon;

    public ComponentBuilder(int x, int y, ItemStackBuilder icon) {
        super(x, y);
        this.icon = icon;
    }

    @Override
    protected ComponentBuilder<T> self() {
        return this;
    }

    public ComponentBuilder(int x, int y, Item item) {
        this(x, y, itemStack(item));
    }

    public ComponentBuilder(int x, int y, PlayerEntity player) {
        this(x, y, skullOf(player));
    }

    public ComponentBuilder<T> withName(String name) {
        icon.withName(name);
        return this;
    }

    public ComponentBuilder<T> withAdditional() {
        icon.withAdditional();
        return this;
    }

    public ComponentBuilder<T> withLore(String loreLine) {
        icon.withLore(loreLine);
        return this;
    }

    public Component<T> build() {
        return new Component<>((y * 9) + x, new Icon(icon.build()), action, navigation);
    }

}
