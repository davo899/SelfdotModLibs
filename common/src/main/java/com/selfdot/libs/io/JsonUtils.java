package com.selfdot.libs.io;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class JsonUtils {

    private static <T> T loadOrDefault(Gson gson, String filename, TypeDef<T> typeDef, T fallback) {
        try (FileReader reader = new FileReader(filename, StandardCharsets.UTF_8)) {
            return typeDef.fromJson(gson, reader);
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

    public static <T> T loadOrDefault(Gson gson, String filename, Class<T> clazz, T fallback) {
        return loadOrDefault(gson, filename, new TypeDef<>(clazz), fallback);
    }

    public static <T> T loadOrDefault(Gson gson, String filename, TypeToken<T> typeToken, T fallback) {
        return loadOrDefault(gson, filename, new TypeDef<>(typeToken), fallback);
    }

}
