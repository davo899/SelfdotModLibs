package com.selfdot.libs.cobblemon;

import com.cobblemon.mod.common.CobblemonItems;
import com.cobblemon.mod.common.api.moves.Move;
import com.cobblemon.mod.common.api.moves.categories.DamageCategories;
import com.cobblemon.mod.common.api.pokemon.PokemonProperties;
import com.cobblemon.mod.common.api.pokemon.evolution.PreEvolution;
import com.cobblemon.mod.common.api.pokemon.stats.Stat;
import com.cobblemon.mod.common.api.pokemon.stats.Stats;
import com.cobblemon.mod.common.api.types.ElementalType;
import com.cobblemon.mod.common.api.types.ElementalTypes;
import com.cobblemon.mod.common.item.PokemonItem;
import com.cobblemon.mod.common.pokemon.*;
import com.cobblemon.mod.common.pokemon.evolution.StandardPreEvolution;
import com.selfdot.libs.minecraft.screen.ItemStackBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.*;

import static com.cobblemon.mod.common.CobblemonItems.*;
import static com.selfdot.libs.minecraft.screen.ItemStackBuilder.itemStack;
import static net.minecraft.util.Formatting.*;

public class CobblemonUtils {

    public static boolean translationFailed(String result) {
        return result.contains("cobblemon.");
    }

    public static ItemStackBuilder speciesItem(Species species) {
        return speciesItem(species, Set.of());
    }

    public static ItemStackBuilder speciesItem(Species species, Set<String> aspects) {
        return itemStack(PokemonItem.from(species, aspects, 1, null));
    }

    public static ItemStackBuilder pokemonInfoItem(Pokemon pokemon) {
        IVs ivs = pokemon.getIvs();
        EVs evs = pokemon.getEvs();
        Move[] moves = new Move[4];
        for (int i = 0; i < 4; i++) moves[i] = pokemon.getMoveSet().get(i);

        String formName = "";
        if (!pokemon.getAspects().isEmpty()) {
            formName = "-" + pokemon.getForm().getName();
        }

        String heldItem = "None";
        if (!pokemon.heldItem().getItem().equals(Items.AIR)) heldItem = pokemon.heldItem().getName().getString();

        ItemStackBuilder itemStackBuilder = itemStack(PokemonItem.from(pokemon))
            .withName(
                GRAY + pokemon.getDisplayName().getString() + formName + (pokemon.getShiny() ? GOLD + " ★" : "")
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
            .withLore(GRAY + "Held Item: " + WHITE + heldItem)
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
                WHITE + "-" + (moves[i] == null ? "Empty" : moves[i].getDisplayName().getString())
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

    public static Item typeGem(ElementalType type) {
        if      (type.equals(ElementalTypes.INSTANCE.getGROUND()))   return GROUND_GEM;
        else if (type.equals(ElementalTypes.INSTANCE.getROCK()))     return ROCK_GEM;
        else if (type.equals(ElementalTypes.INSTANCE.getFIGHTING())) return FIGHTING_GEM;
        else if (type.equals(ElementalTypes.INSTANCE.getPOISON()))   return POISON_GEM;
        else if (type.equals(ElementalTypes.INSTANCE.getGHOST()))    return GHOST_GEM;
        else if (type.equals(ElementalTypes.INSTANCE.getDRAGON()))   return DRAGON_GEM;
        else if (type.equals(ElementalTypes.INSTANCE.getICE()))      return ICE_GEM;
        else if (type.equals(ElementalTypes.INSTANCE.getFLYING()))   return FLYING_GEM;
        else if (type.equals(ElementalTypes.INSTANCE.getGRASS()))    return GRASS_GEM;
        else if (type.equals(ElementalTypes.INSTANCE.getBUG()))      return BUG_GEM;
        else if (type.equals(ElementalTypes.INSTANCE.getFIRE()))     return FIRE_GEM;
        else if (type.equals(ElementalTypes.INSTANCE.getELECTRIC())) return ELECTRIC_GEM;
        else if (type.equals(ElementalTypes.INSTANCE.getPSYCHIC()))  return PSYCHIC_GEM;
        else if (type.equals(ElementalTypes.INSTANCE.getSTEEL()))    return STEEL_GEM;
        else if (type.equals(ElementalTypes.INSTANCE.getWATER()))    return WATER_GEM;
        else if (type.equals(ElementalTypes.INSTANCE.getDARK()))     return DARK_GEM;
        else if (type.equals(ElementalTypes.INSTANCE.getFAIRY()))    return FAIRY_GEM;
        else if (type.equals(ElementalTypes.INSTANCE.getNORMAL()))   return STEEL_GEM;
        return Items.DIAMOND;
    }

    private static final int MAX_LORE_LENGTH = 30;
    public static ItemStackBuilder moveItem(Move move) {
        boolean isStatus = false;
        String damageCategory;
        if (move.getDamageCategory().getName().equals(DamageCategories.INSTANCE.getPHYSICAL().getName())) {
            damageCategory = Formatting.RED + "Physical";
        } else if (move.getDamageCategory().getName().equals(DamageCategories.INSTANCE.getSPECIAL().getName())) {
            damageCategory = Formatting.LIGHT_PURPLE + "Special";
        } else {
            damageCategory = Formatting.GRAY + "Status";
            isStatus = true;
        }

        ItemStackBuilder itemStackBuilder = itemStack(typeGem(move.getType()))
            .withName(move.getDisplayName().getString())
            .withLore(typeString(move.getType()))
            .withLore(damageCategory);
        if (!isStatus) itemStackBuilder.withLore(GOLD + "Power: " + (int)move.getPower());

        String description = move.getDescription().getString();
        if (!translationFailed(description)) {
            Deque<String> words = new ArrayDeque<>(List.of(description.split(" ")));
            if (!words.isEmpty()) {
                StringBuilder stringBuilder = new StringBuilder(words.removeFirst());
                while (!words.isEmpty()) {
                    if (stringBuilder.length() >= MAX_LORE_LENGTH) {
                        itemStackBuilder.withLore(GRAY + stringBuilder.toString());
                        stringBuilder = new StringBuilder(words.removeFirst());
                    } else {
                        stringBuilder.append(" ");
                        stringBuilder.append(words.removeFirst());
                    }
                }
                itemStackBuilder.withLore(GRAY + stringBuilder.toString());
            }
        }
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

    public static ItemStackBuilder natureItem(Nature nature) {
        ItemStackBuilder itemStack = itemStack(Items.CYAN_DYE)
            .withName(Text.translatable(nature.getDisplayName()).getString());
        Stat increasedStat = nature.getIncreasedStat();
        Stat decreasedStat = nature.getDecreasedStat();
        if (increasedStat == null || decreasedStat == null) {
            itemStack.withLore(Formatting.GRAY + "No stat change");
        } else {
            itemStack.withLore(Formatting.GREEN + "+" + increasedStat.getDisplayName().getString())
                .withLore(Formatting.RED + "-" + decreasedStat.getDisplayName().getString());
        }
        return itemStack;
    }

}
