package com.selfdot.libs.minecraft.screen;

public class DynamicComponentBuilder<T extends Menu<T>> extends AbstractComponentBuilder<T, DynamicComponentBuilder<T>> {

    private final DynamicIcon icon;

    public DynamicComponentBuilder(int x, int y, DynamicIcon icon) {
        super(x, y);
        this.icon = icon;
    }

    @Override
    protected DynamicComponentBuilder<T> self() {
        return this;
    }

    public Component<T> build() {
        return new Component<>((y * 9) + x, icon, action, navigation);
    }

}
