package com.selfdot.libs.minecraft;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.server.command.ServerCommandSource;

import java.util.Arrays;

@Slf4j
public abstract class CaughtCommand implements Command<ServerCommandSource> {

    protected abstract int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException;

    @Override
    public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        try {
            return execute(context);
        } catch (CommandSyntaxException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Throwable t) {
            log.error(String.valueOf(t));
            log.error(t.getMessage());
            log.error(Arrays.toString(t.getStackTrace()));
        }
        return 0;
    }

}
