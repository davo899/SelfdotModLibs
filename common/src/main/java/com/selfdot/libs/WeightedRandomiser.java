package com.selfdot.libs;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WeightedRandomiser<T> {

    private static final Random RANDOM = new Random();

    private final List<Pair<T, Double>> items = new ArrayList<>();
    private double totalWeight = 0;

    public void add(T item, double weight) {
        if (weight <= 0) throw new IllegalArgumentException("Weight must be positive");
        items.add(new Pair<>(item, weight));
        totalWeight += weight;
    }

    public T roll() {
        if (totalWeight <= 0) return null;
        double roll = RANDOM.nextDouble(totalWeight);
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
