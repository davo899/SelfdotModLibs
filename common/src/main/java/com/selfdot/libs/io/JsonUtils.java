package com.selfdot.libs.io;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
public class JsonUtils {

    public static void createDirectories(Path path) {
        try {
            if (!Files.exists(path)) Files.createDirectories(path);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static <T> T loadOrDefault(Gson gson, String filename, TypeDef<T> typeDef, T fallback) {
        createDirectories(Path.of(filename));
        try (FileReader reader = new FileReader(filename, StandardCharsets.UTF_8)) {
            T object = typeDef.fromJson(gson, reader);
            log.info("Loaded " + filename);
            return object;
        } catch (FileNotFoundException e) {
            log.warn(filename + " not found");
            try (FileWriter writer = new FileWriter(filename, StandardCharsets.UTF_8)) {
                gson.toJson(fallback, writer);
                writer.flush();
                log.warn("Generated default for " + filename);
            } catch (IOException e2) {
                log.error("Could not generate default for " + filename);
                e2.printStackTrace();
            }
        } catch (IOException | JsonSyntaxException e) {
            log.error("Could not load " + filename);
            e.printStackTrace();
        }
        log.warn("Using fallback object as substitute for " + filename);
        return fallback;
    }

    public static <T> T loadOrDefault(Gson gson, String filename, Class<T> clazz, T fallback) {
        return loadOrDefault(gson, filename, new TypeDef<>(clazz), fallback);
    }

    public static <T> T loadOrDefault(Gson gson, String filename, Type type, T fallback) {
        return loadOrDefault(gson, filename, new TypeDef<>(type), fallback);
    }

}
