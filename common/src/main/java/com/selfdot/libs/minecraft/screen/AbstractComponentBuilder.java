package com.selfdot.libs.minecraft.screen;

import java.util.function.Consumer;

abstract class AbstractComponentBuilder<T extends Menu<T>, B extends AbstractComponentBuilder<T, B>> {

    protected final int x;
    protected final int y;
    protected Consumer<T> action = menu -> {};
    protected Consumer<T> navigation = menu -> {};

    protected AbstractComponentBuilder(int x, int y) {
        this.x = x;
        this.y = y;
    }

    protected abstract B self();

    public B withAction(Consumer<T> action) {
        this.action = action;
        return self();
    }

    public B navigatesTo(String view) {
        this.navigation = menu -> menu.navigate(view);
        return self();
    }

    public B refreshes() {
        this.navigation = Menu::refresh;
        return self();
    }

}
