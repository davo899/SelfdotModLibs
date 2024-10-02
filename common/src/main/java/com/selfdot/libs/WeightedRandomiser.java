package com.selfdot.libs;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WeightedRandomiser<T> {

    private final Random random;
    private final List<Pair<T, Double>> items = new ArrayList<>();
    private double totalWeight = 0;

    public WeightedRandomiser() {
        this.random = new Random();
    }

    public WeightedRandomiser(long seed) {
        this.random = new Random(seed);
    }

    public void setSeed(long seed) {
        random.setSeed(seed);
    }

    public void add(T item, double weight) {
        if (weight <= 0) throw new IllegalArgumentException("Weight must be positive");
        items.add(new Pair<>(item, weight));
        totalWeight += weight;
    }

    public T roll() {
        if (totalWeight <= 0) return null;
        double roll = random.nextDouble(totalWeight);
        double runningTotal = 0;
        for (Pair<T, Double> item : items) {
            runningTotal += item.right();
            if (roll < runningTotal) return item.left();
        }
        return null;
    }

    public void clear() {
        items.clear();
        totalWeight = 0;
    }

}
