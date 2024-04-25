package com.selfdot.libs.minecraft.permissions;

import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.command.CommandSource;

public class FabricPermissionValidator implements PermissionValidator {

    @Override
    public boolean hasPermission(CommandSource source, Permission permission) {
        return Permissions.check(source, "selfdot." + permission.literal(), permission.level().ordinal());
    }

}
