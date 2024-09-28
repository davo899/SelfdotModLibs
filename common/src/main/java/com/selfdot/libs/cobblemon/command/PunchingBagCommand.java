package com.selfdot.libs.cobblemon.command;

import com.cobblemon.mod.common.CobblemonEntities;
import com.cobblemon.mod.common.api.moves.Moves;
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.battles.BattleBuilder;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.Species;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.selfdot.libs.minecraft.command.ServerCommand;
import com.selfdot.libs.minecraft.permissions.Permission;
import com.selfdot.libs.minecraft.permissions.PermissionLevel;
import com.selfdot.libs.minecraft.permissions.PermissionValidator;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

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

        Pokemon punchingBag = new Pokemon();
        punchingBag.setSpecies(blissey);
        punchingBag.setLevel(100);
        punchingBag.getMoveSet().setMove(0, Moves.INSTANCE.getByNameOrDummy("splash").create());
        punchingBag.getMoveSet().setMove(1, Moves.INSTANCE.getByNameOrDummy("holdhands").create());
        punchingBag.getMoveSet().setMove(2, Moves.INSTANCE.getByNameOrDummy("celebrate").create());
        punchingBag.getMoveSet().setMove(3, Moves.INSTANCE.getByNameOrDummy("batonpass").create());

        PokemonEntity punchingBagEntity = new PokemonEntity(player.getServerWorld(), punchingBag, CobblemonEntities.POKEMON);
        BlockPos pos = player.getBlockPos();
        punchingBagEntity.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
        player.getServerWorld().spawnEntity(punchingBagEntity);
        BattleBuilder.INSTANCE.pve(player, punchingBagEntity);
        return SINGLE_SUCCESS;
    }

}
