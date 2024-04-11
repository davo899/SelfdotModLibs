package com.selfdot.libs.cobblemon;

import com.cobblemon.mod.common.CobblemonItems;
import com.cobblemon.mod.common.api.moves.Move;
import com.cobblemon.mod.common.api.moves.categories.DamageCategories;
import com.cobblemon.mod.common.api.pokemon.stats.Stats;
import com.cobblemon.mod.common.api.types.ElementalType;
import com.cobblemon.mod.common.api.types.ElementalTypes;
import com.cobblemon.mod.common.item.PokemonItem;
import com.cobblemon.mod.common.pokemon.EVs;
import com.cobblemon.mod.common.pokemon.IVs;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.Species;
import com.selfdot.libs.minecraft.screen.ItemStackBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

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

    public static String typeString(ElementalType type) {
        Formatting colour = Formatting.WHITE;
        if      (type.equals(ElementalTypes.INSTANCE.getGROUND()))   colour = GOLD;
        else if (type.equals(ElementalTypes.INSTANCE.getROCK()))     colour = GOLD;
        else if (type.equals(ElementalTypes.INSTANCE.getFIGHTING())) colour = GOLD;
        else if (type.equals(ElementalTypes.INSTANCE.getPOISON()))   colour = Formatting.DARK_PURPLE;
        else if (type.equals(ElementalTypes.INSTANCE.getGHOST()))    colour = Formatting.DARK_PURPLE;
        else if (type.equals(ElementalTypes.INSTANCE.getDRAGON()))   colour = Formatting.DARK_PURPLE;
        else if (type.equals(ElementalTypes.INSTANCE.getICE()))      colour = Formatting.AQUA;
        else if (type.equals(ElementalTypes.INSTANCE.getFLYING()))   colour = Formatting.AQUA;
        else if (type.equals(ElementalTypes.INSTANCE.getGRASS()))    colour = Formatting.GREEN;
        else if (type.equals(ElementalTypes.INSTANCE.getBUG()))      colour = Formatting.GREEN;
        else if (type.equals(ElementalTypes.INSTANCE.getFIRE()))     colour = Formatting.RED;
        else if (type.equals(ElementalTypes.INSTANCE.getELECTRIC())) colour = Formatting.YELLOW;
        else if (type.equals(ElementalTypes.INSTANCE.getPSYCHIC()))  colour = Formatting.LIGHT_PURPLE;
        else if (type.equals(ElementalTypes.INSTANCE.getSTEEL()))    colour = Formatting.GRAY;
        else if (type.equals(ElementalTypes.INSTANCE.getWATER()))    colour = Formatting.BLUE;
        else if (type.equals(ElementalTypes.INSTANCE.getDARK()))     colour = Formatting.DARK_PURPLE;
        return colour + type.getDisplayName().getString();
    }

    public static ItemStackBuilder moveItem(Move move) {
        boolean isStatus = false;
        Item item;
        String damageCategory;
        if (move.getDamageCategory().getName().equals(DamageCategories.INSTANCE.getPHYSICAL().getName())) {
            item = Items.MUSIC_DISC_BLOCKS;
            damageCategory = Formatting.RED + "Physical";
        } else if (move.getDamageCategory().getName().equals(DamageCategories.INSTANCE.getSPECIAL().getName())) {
            item = Items.MUSIC_DISC_MALL;
            damageCategory = Formatting.LIGHT_PURPLE + "Special";
        } else {
            item = Items.MUSIC_DISC_STRAD;
            damageCategory = Formatting.GRAY + "Status";
            isStatus = true;
        }

        ItemStackBuilder itemStackBuilder = itemStack(item)
            .withName(move.getDisplayName().getString())
            .withLore(typeString(move.getType()))
            .withLore(damageCategory);
        if (!isStatus) itemStackBuilder.withLore(GOLD + "Power: " + (int)move.getPower());
        return itemStackBuilder;
    }

    public static Item statVitaminItem(Stats stat) {
        return switch (stat) {
            case HP -> CobblemonItems.HP_UP;
            case ATTACK -> CobblemonItems.PROTEIN;
            case DEFENCE -> CobblemonItems.IRON;
            case SPECIAL_ATTACK -> CobblemonItems.CALCIUM;
            case SPECIAL_DEFENCE -> CobblemonItems.ZINC;
            case SPEED -> CobblemonItems.CARBOS;
            default -> CobblemonItems.CHARCOAL;
        };
    }

    public static ItemStackBuilder statVitaminItemStack(Stats stat) {
        return itemStack(statVitaminItem(stat)).withName(stat.getDisplayName().getString());
    }

}
