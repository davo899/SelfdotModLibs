package com.selfdot.libs.minecraft;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public record Box(ServerPosition corner, Vec3i size) {

    public static Box fromCorners(ServerPosition corner1, ServerPosition corner2) {
        if (!corner1.getDimension().equals(corner2.getDimension())) return null;
        return new Box(
            new ServerPosition(
                Math.min(corner1.getX(), corner2.getX()),
                Math.min(corner1.getY(), corner2.getY()),
                Math.min(corner1.getZ(), corner2.getZ()),
                corner1.getDimension()
            ),
            new Vec3i(
                Math.abs(corner1.getX() - corner2.getX()),
                Math.abs(corner1.getY() - corner2.getY()),
                Math.abs(corner1.getZ() - corner2.getZ())
            )
        );
    }

    public boolean contains(double x, double y, double z) {
        return x >= corner.getX() && x <= corner.getX() + size.getX() &&
            y >= corner.getY() && y <= corner.getY() + size.getY() &&
            z >= corner.getZ() && z <= corner.getZ() + size.getZ();
    }

    public boolean contains(Vec3i vec3i) {
        return contains(vec3i.getX(), vec3i.getY(), vec3i.getZ());
    }

    public boolean contains(BlockPos blockPos) {
        return contains(blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }

    public boolean contains(Vec3d vec3d) {
        return contains(vec3d.getX(), vec3d.getY(), vec3d.getZ());
    }

}
