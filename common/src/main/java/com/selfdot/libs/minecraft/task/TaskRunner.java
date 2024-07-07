package com.selfdot.libs.minecraft.task;

import net.minecraft.server.MinecraftServer;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class TaskRunner {

    private static final TaskRunner INSTANCE = new TaskRunner();

    private TaskRunner() { }

    public static TaskRunner getInstance() {
        return INSTANCE;
    }

    private final Queue<Task> taskQueue = new PriorityQueue<>(Comparator.comparingDouble(Task::tick));
    private int currentTick = 0;

    public void onTick(MinecraftServer server) {
        while (!taskQueue.isEmpty() && taskQueue.peek().tick() <= currentTick) taskQueue.poll().task().run();
        currentTick++;
    }

    public void runLater(Runnable task, double ticks) {
        taskQueue.add(new Task(task, currentTick + ticks));
    }

}
