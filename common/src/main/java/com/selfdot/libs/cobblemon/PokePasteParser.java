package com.selfdot.libs.cobblemon;

import com.cobblemon.mod.common.api.abilities.Abilities;
import com.cobblemon.mod.common.api.abilities.Ability;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.aspects.PokemonAspectsKt;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PokePasteParser {

    private static final Map<String, Ability> ABILITIES = new HashMap<>();
    static {
        Abilities.INSTANCE.all().forEach(abilityTemplate -> ABILITIES.put(
            Text.translatable(abilityTemplate.getDisplayName()).getString(),
            new Ability(abilityTemplate, false)
        ));
    }

    public static List<Pokemon> parse(String pokePaste) {
        List<Pokemon> team = new ArrayList<>();
        for (String block : pokePaste.split("\\n\\n+")) {
            team.add(parsePokemon(block.trim()));
        }
        return team;
    }

    private static Pokemon parsePokemon(String block) {
        Pokemon pokemon = new Pokemon();
        String[] lines = block.split("\\n");

        parseFirstLine(pokemon, lines[0]);

        for (int i = 1; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.startsWith("Ability:")) {
                pokemon.updateAbility(ABILITIES.get(line.substring(9).trim()));
            } else if (line.startsWith("EVs:")) {
                parseEVs(pokemon, line.substring(5).trim());
            } else if (line.startsWith("IVs:")) {
                parseIVs(pokemon, line.substring(5).trim());
            } else if (line.equalsIgnoreCase("Shiny: Yes")) {
                PokemonAspectsKt.getSHINY_ASPECT().provide(pokemon);
                pokemon.updateAspects();
            } else if (line.startsWith("Level:")) {
                pokemon.setLevel(Integer.parseInt(line.substring(7).trim()));
            } else if (line.startsWith("Happiness:")) {
                pokemon.setFriendship(Integer.parseInt(line.substring(11).trim()), true);
            } else if (line.endsWith(" Nature")) {
                pokemon.nature = line.split(" ")[0];
            } else if (line.startsWith("-")) {
                pokemon.moves.add(line.substring(1).trim());
            }
        }

        return pokemon;
    }

    private static void parseFirstLine(Pokemon pokemon, String line) {
        Pattern pattern = Pattern.compile("^(.*?)(?: \\((.*?)\\))?(?: \\((F|M)\\))?(?: @ (.*))?$");
        Matcher matcher = pattern.matcher(line);
        if (matcher.matches()) {
            pokemon.nickname = matcher.group(1);
            pokemon.name = matcher.group(2) != null ? matcher.group(2) : matcher.group(1);
            pokemon.gender = matcher.group(3);
            pokemon.item = matcher.group(4);
        }
    }

    private static void parseEVs(Pokemon pokemon, String line) {
        String[] parts = line.split(" / ");
        for (String part : parts) {
            String[] evParts = part.split(" ");
            int value = Integer.parseInt(evParts[0]);
            switch (evParts[1]) {
                case "HP":
                    pokemon.evs[0] = value;
                    break;
                case "Atk":
                    pokemon.evs[1] = value;
                    break;
                case "Def":
                    pokemon.evs[2] = value;
                    break;
                case "SpA":
                    pokemon.evs[3] = value;
                    break;
                case "SpD":
                    pokemon.evs[4] = value;
                    break;
                case "Spe":
                    pokemon.evs[5] = value;
                    break;
            }
        }
    }

    private static void parseIVs(Pokemon pokemon, String line) {
        String[] parts = line.split(" / ");
        for (String part : parts) {
            String[] ivParts = part.split(" ");
            int value = Integer.parseInt(ivParts[0]);
            switch (ivParts[1]) {
                case "HP":
                    pokemon.ivs[0] = value;
                    break;
                case "Atk":
                    pokemon.ivs[1] = value;
                    break;
                case "Def":
                    pokemon.ivs[2] = value;
                    break;
                case "SpA":
                    pokemon.ivs[3] = value;
                    break;
                case "SpD":
                    pokemon.ivs[4] = value;
                    break;
                case "Spe":
                    pokemon.ivs[5] = value;
                    break;
            }
        }
    }

    public static void main(String[] args) {
        String pokePaste = """
            aaaaaa (Calyrex-Shadow) @ Choice Scarf \s
            Ability: As One (Spectrier) \s
            Tera Type: Psychic \s
            EVs: 124 HP / 84 Atk / 92 SpA / 208 Spe \s
            IVs: 0 Atk \s
            - Future Sight \s
            - Curse \s
            - Draining Kiss \s
            - Baton Pass \s
                    
            brrrr (Arceus-Psychic) @ Mind Plate \s
            Ability: Multitype \s
            Tera Type: Psychic \s
            EVs: 252 HP / 252 SpA / 4 Spe \s
            - Judgment \s
            - Brick Break \s
            - Draco Meteor \s
            - Earth Power \s
                    
            sppeeeed (Deoxys-Speed) @ Assault Vest \s
            Ability: Pressure \s
            Tera Type: Psychic \s
            EVs: 136 Def / 120 SpD / 252 Spe \s
            IVs: 0 Atk \s
            - Calm Mind \s
            - Ice Beam \s
            - Meteor Beam \s
            - Night Shade \s
                    
            oh (Ho-Oh) @ Rocky Helmet \s
            Ability: Regenerator \s
            Tera Type: Fire \s
            EVs: 252 HP / 252 Atk / 4 Def \s
            IVs: 0 Atk \s
            - Air Slash \s
            - Fire Blast \s
            - Protect \s
            - Future Sight \s
                    
            Giratina-Origin @ Griseous Core \s
            Ability: Levitate \s
            Tera Type: Ghost \s
            EVs: 252 HP / 4 SpA / 252 SpD \s
            - Defog \s
            - Earthquake \s
            - Protect \s
            - Dragon Pulse \s
                    
            leaf (Voltorb-Hisui) @ Sitrus Berry \s
            Ability: Static \s
            Tera Type: Electric \s
            EVs: 252 HP / 172 SpA / 84 SpD \s
            IVs: 0 Atk \s
            - Energy Ball \s
            - Grass Knot \s
            - Leaf Storm \s
            - Thunder \s
        """;

        List<Pokemon> team = parse(pokePaste);
        for (Pokemon pokemon : team) {
            System.out.println(pokemon);
        }
    }
}
