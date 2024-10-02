package com.selfdot.libs.fabric.test;

import com.selfdot.libs.minecraft.task.TaskRunner;
import lombok.extern.slf4j.Slf4j;
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class SelfdotModLibsFabricTest implements FabricGameTest {

    @Override
    public void invokeTestMethod(TestContext context, Method method) {
        new TestFabricMod().onInitialize();
        FabricGameTest.super.invokeTestMethod(context, method);
    }

    @GameTest(templateName = EMPTY_STRUCTURE)
    public void taskRunnerIsTicked(TestContext context) {
        AtomicBoolean success = new AtomicBoolean(false);
        context.runAtTick(1, () -> TaskRunner.getInstance().runLater(() -> success.set(true), 2));
        context.runAtTick(4, () -> {
            context.assertTrue(success.get(), "TaskRunner did not tick");
            context.complete();
        });
    }

}
