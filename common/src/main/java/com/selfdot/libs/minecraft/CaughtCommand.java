package com.selfdot.libs.minecraft;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.server.command.ServerCommandSource;

import java.util.Arrays;

@Slf4j
public abstract class CaughtCommand implements Command<ServerCommandSource> {

    protected abstract int execute(CommandContext<ServerCommandSource> context);

    @Override
    public int run(CommandContext<ServerCommandSource> context) {
        try {
            return execute(context);
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
