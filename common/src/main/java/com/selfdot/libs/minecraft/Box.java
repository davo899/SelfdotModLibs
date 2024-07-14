package com.selfdot.libs.minecraft;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public record Box(ServerPosition corner, Vec3i size) {

    public static Box fromCorners(ServerPosition corner1, ServerPosition corner2) {
        if (!corner1.dimension().equals(corner2.dimension())) return null;
        return new Box(
            new ServerPosition(
                Math.min(corner1.x(), corner2.x()),
                Math.min(corner1.y(), corner2.y()),
                Math.min(corner1.z(), corner2.z()),
                corner1.dimension()
            ),
            new Vec3i(
                Math.abs(corner1.x() - corner2.x()),
                Math.abs(corner1.y() - corner2.y()),
                Math.abs(corner1.z() - corner2.z())
            )
        );
    }

    public boolean contains(double x, double y, double z) {
        return x >= corner.x() && x <= corner.x() + size.getX() &&
            y >= corner.y() && y <= corner.y() + size.getY() &&
            z >= corner.z() && z <= corner.z() + size.getZ();
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
