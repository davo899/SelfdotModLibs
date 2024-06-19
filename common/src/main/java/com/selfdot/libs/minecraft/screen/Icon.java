package com.selfdot.libs.minecraft.screen;

import lombok.Getter;
import net.minecraft.item.ItemStack;

public class Icon {

    @Getter
    protected ItemStack itemStack;

    public Icon(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public boolean tick() {
        return false;
    }

}
