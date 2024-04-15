package com.selfdot.libs.minecraft;

import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

import static net.minecraft.util.Formatting.*;

public class MinecraftUtils {

    public static void spawnFireworkExplosion(
        Vec3d position,
        World world,
        DyeColor primaryColor,
        FireworkRocketItem.Type type,
        DyeColor fadeColor
    ) {
        ItemStack itemStack = new ItemStack(Items.FIREWORK_ROCKET, 1);
        ItemStack itemStack2 = new ItemStack(Items.FIREWORK_STAR);
        NbtCompound nbtCompound = itemStack2.getOrCreateSubNbt("Explosion");
        nbtCompound.putIntArray("Colors", List.of(primaryColor.getFireworkColor()));
        nbtCompound.putIntArray("FadeColors", List.of(fadeColor.getFireworkColor()));
        nbtCompound.putByte("Type", (byte)type.getId());
        nbtCompound.putByte("Flicker", (byte)1);
        NbtCompound nbtCompound2 = itemStack.getOrCreateSubNbt("Fireworks");
        NbtList nbtList = new NbtList();
        NbtCompound nbtCompound3 = itemStack2.getSubNbt("Explosion");
        if (nbtCompound3 != null) nbtList.add(nbtCompound3);
        if (!nbtList.isEmpty()) nbtCompound2.put("Explosions", nbtList);

        FireworkRocketEntity fireworkRocketEntity = new FireworkRocketEntity(
            world,
            position.getX(),
            position.getY(),
            position.getZ(),
            itemStack
        );
        world.spawnEntity(fireworkRocketEntity);
        world.sendEntityStatus(fireworkRocketEntity, (byte)17);
        fireworkRocketEntity.discard();
    }

    public static String colourize(String string) {
        return string
            .replace("&0", BLACK.asString()).replace("&1", DARK_BLUE.asString())
            .replace("&2", DARK_GREEN.asString()).replace("&3", DARK_AQUA.asString())
            .replace("&4", DARK_RED.asString()).replace("&5", DARK_PURPLE.asString())
            .replace("&6", GOLD.asString()).replace("&7", GRAY.asString())
            .replace("&8", DARK_GRAY.asString()).replace("&9", BLUE.asString())
            .replace("&a", GREEN.asString()).replace("&b", AQUA.asString())
            .replace("&c", RED.asString()).replace("&d", LIGHT_PURPLE.asString())
            .replace("&e", YELLOW.asString()).replace("&f", WHITE.asString())
            .replace("&k", OBFUSCATED.asString()).replace("&l", BOLD.asString())
            .replace("&m", STRIKETHROUGH.asString()).replace("&n", UNDERLINE.asString())
            .replace("&o", ITALIC.asString()).replace("&r", RESET.asString())
            .replace("&A", GREEN.asString()).replace("&B", AQUA.asString())
            .replace("&C", RED.asString()).replace("&D", LIGHT_PURPLE.asString())
            .replace("&E", YELLOW.asString()).replace("&F", WHITE.asString())
            .replace("&K", OBFUSCATED.asString()).replace("&L", BOLD.asString())
            .replace("&M", STRIKETHROUGH.asString()).replace("&N", UNDERLINE.asString())
            .replace("&O", ITALIC.asString()).replace("&R", RESET.asString());
    }

}
