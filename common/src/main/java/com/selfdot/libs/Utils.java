package com.selfdot.libs;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class Utils {

    public static List<String> textWrap(String text, int limit) {
        List<String> lines = new ArrayList<>();
        Deque<String> words = new ArrayDeque<>(List.of(text.split(" ")));
        if (!words.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder(words.removeFirst());
            while (!words.isEmpty()) {
                if (stringBuilder.length() >= limit) {
                    lines.add(stringBuilder.toString());
                    stringBuilder = new StringBuilder(words.removeFirst());
                } else {
                    stringBuilder.append(" ");
                    stringBuilder.append(words.removeFirst());
                }
            }
            lines.add(stringBuilder.toString());
        }
        return lines;
    }

}
