package com.selfdot.libs.cobblemon;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.storage.NoPokemonStoreException;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.selfdot.libs.minecraft.screen.ItemElementBuilder;
import com.selfdot.libs.minecraft.screen.ViewFactory;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGuiBuilder;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.function.BiFunction;
import java.util.function.Consumer;

import static com.selfdot.libs.minecraft.screen.ScreenUtils.absolutePosition;
import static net.minecraft.item.Items.GLASS_PANE;
import static net.minecraft.util.Formatting.GRAY;

public class PartyViewFactory extends ViewFactory {

    private final ServerPlayerEntity player;
    private final BiFunction<Pokemon, ItemElementBuilder<?>, ItemElementBuilder<?>> editItem;

    public PartyViewFactory(
        ServerPlayerEntity player,
        BiFunction<Pokemon, ItemElementBuilder<?>, ItemElementBuilder<?>> editItem,
        Consumer<SimpleGuiBuilder> create
    ) {
        super(ScreenHandlerType.GENERIC_9X3, create);
        this.player = player;
        this.editItem = editItem;
    }

    @Override
    protected void build(SimpleGuiBuilder guiBuilder) {
        super.build(guiBuilder);
        PlayerPartyStore party;
        try {
            party = Cobblemon.INSTANCE.getStorage().getParty(player.getUuid());
        } catch (NoPokemonStoreException e) {
            return;
        }
        for (int i = 0; i < party.size(); i++) {
            Pokemon pokemon = party.get(i);
            int x = 1 + i + (i / 3);
            if (pokemon == null) {
                guiBuilder.setSlot(
                    absolutePosition(guiBuilder, x, 2),
                    new GuiElementBuilder()
                        .setItem(GLASS_PANE)
                        .setName(Text.literal(GRAY + "Empty"))
                );
                continue;
            }
            guiBuilder.setSlot(
                absolutePosition(guiBuilder, x, 2),
                editItem.apply(pokemon, new PokemonElementBuilder(pokemon.getSpecies(), pokemon.getAspects()))
            );
        }
    }

}
