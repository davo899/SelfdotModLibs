package com.selfdot.libs.minecraft;

import net.minecraft.util.Identifier;

public record ServerPosition(int x, int y, int z, Identifier dimension) { }
