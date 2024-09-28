package com.selfdot.libs.cobblemon.command;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.battles.model.actor.BattleActor;
import com.cobblemon.mod.common.api.moves.Moves;
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.api.text.TextKt;
import com.cobblemon.mod.common.battles.BattleFormat;
import com.cobblemon.mod.common.battles.BattleSide;
import com.cobblemon.mod.common.battles.BattleStartError;
import com.cobblemon.mod.common.battles.ErroredBattleStart;
import com.cobblemon.mod.common.battles.actor.PlayerBattleActor;
import com.cobblemon.mod.common.battles.actor.TrainerBattleActor;
import com.cobblemon.mod.common.battles.ai.RandomBattleAI;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.Species;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.selfdot.libs.minecraft.command.ServerCommand;
import com.selfdot.libs.minecraft.permissions.Permission;
import com.selfdot.libs.minecraft.permissions.PermissionLevel;
import com.selfdot.libs.minecraft.permissions.PermissionValidator;
import kotlin.Unit;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class PunchingBagCommand extends ServerCommand {

    public PunchingBagCommand(@NotNull PermissionValidator permissionValidator) {
        super(permissionValidator, "punchingbag");
        setRequiresPlayer(true);
        setPermission(new Permission("selfdotmodlibs.punchingbag", PermissionLevel.CHEAT_COMMANDS_AND_COMMAND_BLOCKS));
    }

    @Override
    protected int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        Species blissey = PokemonSpecies.INSTANCE.getByName("blissey");
        if (blissey == null) {
            context.getSource().sendError(Text.literal("Blissey species does not exist"));
            return -1;
        }

        PlayerPartyStore party = Cobblemon.INSTANCE.getStorage().getParty(player);
        UUID leadingPokemon = null;
        for (Pokemon pokemon : party) {
            if (!pokemon.isFainted()) {
                leadingPokemon = pokemon.getUuid();
                break;
            }
        }

        BattleActor playerActor = new PlayerBattleActor(player.getUuid(), party.toBattleTeam(false, true, leadingPokemon));
        BattleFormat battleFormat = BattleFormat.Companion.getGEN_9_SINGLES();

        ErroredBattleStart errors = new ErroredBattleStart();
        Set<BattleStartError> playerErrors = errors.getParticipantErrors().get(playerActor);

        if (playerActor.getPokemonList().size() < battleFormat.getBattleType().getSlotsPerActor()) {
            playerErrors.add(BattleStartError.Companion.insufficientPokemon(
                player,
                battleFormat.getBattleType().getSlotsPerActor(),
                playerActor.getPokemonList().size()
            ));
        }

        if (Cobblemon.INSTANCE.getBattleRegistry().getBattleByParticipatingPlayer(player) != null) {
            playerErrors.add(BattleStartError.Companion.alreadyInBattle(player));
        }

        List<BattlePokemon> trainerTeam = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            Pokemon punchingBag = new Pokemon();
            punchingBag.setSpecies(blissey);
            punchingBag.setLevel(100);
            punchingBag.getMoveSet().setMove(0, Moves.INSTANCE.getByNameOrDummy("splash").create());
            punchingBag.getMoveSet().setMove(1, Moves.INSTANCE.getByNameOrDummy("holdhands").create());
            punchingBag.getMoveSet().setMove(2, Moves.INSTANCE.getByNameOrDummy("celebrate").create());
            punchingBag.getMoveSet().setMove(3, Moves.INSTANCE.getByNameOrDummy("batonpass").create());
            punchingBag.setNickname(Text.literal("Punching Bag"));
            trainerTeam.add(new BattlePokemon(punchingBag, punchingBag, (pokemonEntity -> Unit.INSTANCE)));
        }

        if (errors.isEmpty()) {
            Cobblemon.INSTANCE.getBattleRegistry().startBattle(
                battleFormat,
                new BattleSide(playerActor),
                new BattleSide(new TrainerBattleActor("Punching Bag", UUID.randomUUID(), trainerTeam, new RandomBattleAI())),
                false
            );
            return SINGLE_SUCCESS;

        } else {
            errors.ifErrored(erroredBattleStart -> {
                erroredBattleStart.sendTo(player, TextKt::red);
                return Unit.INSTANCE;
            });
            return -1;
        }
    }

}
