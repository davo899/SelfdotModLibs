package com.selfdot.libs.cobblemon;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.storage.NoPokemonStoreException;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.selfdot.libs.minecraft.screen.ViewFactory;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGuiBuilder;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.function.Consumer;

import static com.selfdot.libs.minecraft.screen.ScreenUtils.absolutePosition;
import static net.minecraft.item.Items.GLASS_PANE;
import static net.minecraft.util.Formatting.GRAY;

public class PartyViewFactory extends ViewFactory {

    private final ServerPlayerEntity player;
    private final Consumer<Pokemon> onClick;

    public PartyViewFactory(
        ServerPlayerEntity player, Consumer<Pokemon> onClick, Consumer<SimpleGuiBuilder> create
    ) {
        super(ScreenHandlerType.GENERIC_9X3, create);
        this.player = player;
        this.onClick = onClick;
    }

    @Override
    protected void build(SimpleGuiBuilder guiBuilder) {
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
                new PokemonElementBuilder(pokemon.getSpecies(), pokemon.getAspects())
                    .setCallback(() -> onClick.accept(pokemon))
            );
        }
        super.build(guiBuilder);
    }

}
