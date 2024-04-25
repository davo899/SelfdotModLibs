package com.selfdot.libs.minecraft.permissions;

import net.minecraft.util.Identifier;

public record Permission(String literal, PermissionLevel level) {

    public Identifier getIdentifier() {
        return new Identifier("selfdot", literal);
    }

}
