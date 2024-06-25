package com.selfdot.libs.io;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class JsonUtils {

    public static <T> T loadOrDefault(Gson gson, String filename, Class<T> clazz, T fallback) {
        try (FileReader reader = new FileReader(filename, StandardCharsets.UTF_8)) {
            return gson.fromJson(reader, clazz);
        } catch (FileNotFoundException e) {
            try (FileWriter writer = new FileWriter(filename, StandardCharsets.UTF_8)) {
                gson.toJson(fallback, writer);
                writer.flush();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        } catch (IOException | JsonSyntaxException e) {
            e.printStackTrace();
        }
        return fallback;
    }

}
