package com.selfdot.libs.minecraft.screen;

import eu.pb4.sgui.api.gui.SimpleGuiBuilder;

public class ScreenUtils {

    private static int clamp(int value, int min, int max) {
        return Math.min(max, Math.max(min, value));
    }

    public static int absolutePosition(SimpleGuiBuilder guiBuilder, int x, int y) {
        return x + (guiBuilder.getWidth() * y);
    }

    public static int relativePosition(SimpleGuiBuilder guiBuilder, float x, float y) {
        return absolutePosition(
            guiBuilder,
            clamp((int)(x * guiBuilder.getWidth()), 0, guiBuilder.getWidth() - 1),
            clamp((int)(y * guiBuilder.getHeight()), 0, guiBuilder.getHeight() - 1)
        );
    }

}
