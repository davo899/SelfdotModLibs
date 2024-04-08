package com.selfdot.libs.cobblemon;

import com.cobblemon.mod.common.api.moves.Move;
import com.cobblemon.mod.common.api.moves.MoveSet;
import com.cobblemon.mod.common.api.pokemon.stats.Stats;
import com.cobblemon.mod.common.item.PokemonItem;
import com.cobblemon.mod.common.pokemon.EVs;
import com.cobblemon.mod.common.pokemon.IVs;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.Species;
import com.selfdot.libs.minecraft.screen.ItemStackBuilder;
import net.minecraft.text.Text;

import static com.selfdot.libs.minecraft.screen.ItemStackBuilder.itemStack;
import static net.minecraft.util.Formatting.*;

public class CobblemonUtils {

    public static ItemStackBuilder speciesItem(Species species) {
        return itemStack(PokemonItem.from(species));
    }

    public static ItemStackBuilder pokemonInfoItem(Pokemon pokemon) {
        IVs ivs = pokemon.getIvs();
        EVs evs = pokemon.getEvs();
        Move[] moves = new Move[4];
        for (int i = 0; i < 4; i++) moves[i] = pokemon.getMoveSet().get(i);

        ItemStackBuilder itemStackBuilder = itemStack(PokemonItem.from(pokemon))
            .withName(
                GRAY + pokemon.getDisplayName().getString() + (pokemon.getShiny() ? GOLD + " ★" : "")
            )
            .withLore(GREEN + "Gender: " + WHITE + pokemon.getGender())
            .withLore(AQUA + "Level: " + WHITE + pokemon.getLevel())
            .withLore(
                YELLOW + "Nature: " +
                    WHITE + Text.translatable(pokemon.getNature().getDisplayName()).getString()
            )
            .withLore(
                GOLD + "Ability: " +
                    WHITE + Text.translatable(pokemon.getAbility().getDisplayName()).getString()
            )
            .withLore(LIGHT_PURPLE + "IVs: ")
            .withLore(String.format(
                "  %5s: %-3s %5s: %-3s %5s: %-3s",
                RED + "HP",
                WHITE + String.valueOf(ivs.getOrDefault(Stats.HP)),
                BLUE + "Atk",
                WHITE + String.valueOf(ivs.getOrDefault(Stats.ATTACK)),
                GRAY + "Def",
                WHITE + String.valueOf(ivs.getOrDefault(Stats.DEFENCE))
            ))
            .withLore(String.format(
                "  %5s: %-3s %5s: %-3s %5s: %-3s",
                AQUA + "SpAtk",
                WHITE + String.valueOf(ivs.getOrDefault(Stats.SPECIAL_ATTACK)),
                YELLOW + "SpDef",
                WHITE + String.valueOf(ivs.getOrDefault(Stats.SPECIAL_DEFENCE)),
                GREEN + "Speed",
                WHITE + String.valueOf(ivs.getOrDefault(Stats.SPEED))
            ))
            .withLore(DARK_AQUA + "EVs: ")
            .withLore(String.format(
                "  %6s: %-4s %6s: %-4s %6s: %-4s",
                RED + "HP",
                WHITE + String.valueOf(evs.getOrDefault(Stats.HP)),
                BLUE + "Atk",
                WHITE + String.valueOf(evs.getOrDefault(Stats.ATTACK)),
                GRAY + "Def",
                WHITE + String.valueOf(evs.getOrDefault(Stats.DEFENCE))
            ))
            .withLore(String.format(
                "  %6s: %-4s %6s: %-4s %6s: %-4s",
                AQUA + "SpAtk",
                WHITE + String.valueOf(evs.getOrDefault(Stats.SPECIAL_ATTACK)),
                YELLOW + "SpDef",
                WHITE + String.valueOf(evs.getOrDefault(Stats.SPECIAL_DEFENCE)),
                GREEN + "Speed",
                WHITE + String.valueOf(evs.getOrDefault(Stats.SPEED))
            ))
            .withLore(DARK_GREEN + "Moves: ");
        for (int i = 0; i < 4; i++) {
            itemStackBuilder.withLore(
                WHITE + "\t" + (moves[i] == null ? "Empty" : moves[i].getDisplayName().getString())
            );
        }
        return itemStackBuilder;
    }

}
