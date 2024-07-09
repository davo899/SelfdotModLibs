package com.selfdot.libs.cobblemon;

import com.cobblemon.mod.common.pokemon.Species;
import com.selfdot.libs.minecraft.screen.ItemElementBuilder;
import com.selfdot.libs.minecraft.screen.ItemStackBuilder;
import eu.pb4.sgui.api.elements.AnimatedGuiElement;
import eu.pb4.sgui.api.elements.GuiElementInterface;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.selfdot.libs.cobblemon.CobblemonUtils.speciesItem;

public class AnimatedPokemonElementBuilder extends ItemElementBuilder<AnimatedPokemonElementBuilder> {

    private static final int INTERVAL_TICKS = 20;

    private final List<ItemStackBuilder> itemStackBuilders = new ArrayList<>();

    public AnimatedPokemonElementBuilder(List<Pair<Species, Set<String>>> pokemon) {
        for (Pair<Species, Set<String>> speciesAspectsPair : pokemon) {
            itemStackBuilders.add(speciesItem(speciesAspectsPair.getLeft(), speciesAspectsPair.getRight()));
        }
    }

    @Override
    public AnimatedPokemonElementBuilder withName(String name) {
        itemStackBuilders.forEach(itemStackBuilder -> itemStackBuilder.withName(name));
        return this;
    }

    @Override
    public AnimatedPokemonElementBuilder withLore(String lore) {
        itemStackBuilders.forEach(itemStackBuilder -> itemStackBuilder.withLore(lore));
        return this;
    }

    @Override
    protected AnimatedPokemonElementBuilder self() {
        return this;
    }

    @Override
    public GuiElementInterface build() {
        List<ItemStack> itemStacks = new ArrayList<>();
        itemStackBuilders.forEach(itemStackBuilder -> itemStacks.add(itemStackBuilder.build()));
        return new AnimatedGuiElement(itemStacks.toArray(new ItemStack[0]), INTERVAL_TICKS, false, callback);
    }

}
