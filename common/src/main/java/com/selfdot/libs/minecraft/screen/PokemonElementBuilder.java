package com.selfdot.libs.minecraft.screen;

import com.cobblemon.mod.common.pokemon.Species;
import eu.pb4.sgui.api.elements.GuiElementInterface;

import java.util.Set;

import static com.selfdot.libs.cobblemon.CobblemonUtils.speciesItem;

public class PokemonElementBuilder extends ItemElementBuilder<PokemonElementBuilder> {

    private final ItemStackBuilder itemStack;

    public PokemonElementBuilder(Species species, Set<String> aspects) {
        this.itemStack = speciesItem(species, aspects);
    }

    public PokemonElementBuilder withName(String name) {
        itemStack.withName(name);
        return this;
    }

    public PokemonElementBuilder withLore(String lore) {
        itemStack.withLore(lore);
        return this;
    }

    @Override
    protected PokemonElementBuilder self() {
        return this;
    }

    @Override
    public GuiElementInterface build() {
        return new PokemonElement(itemStack, callback);
    }

}
