package com.selfdot.libs.minecraft.permissions;

import net.minecraft.command.CommandSource;

public interface PermissionValidator {

    boolean hasPermission(CommandSource source, Permission permission);

}
