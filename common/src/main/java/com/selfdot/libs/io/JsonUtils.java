package com.selfdot.libs.io;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.FileReader;
import java.io.IOException;

public class JsonUtils {

    public <T> T loadOrDefault(Gson gson, String filename, Class<T> clazz, T fallback) {
        try (FileReader reader = new FileReader(filename)) {
            return gson.fromJson(reader, clazz);
        } catch (IOException | JsonSyntaxException e) {
            e.printStackTrace();
        }
        return fallback;
    }

}
