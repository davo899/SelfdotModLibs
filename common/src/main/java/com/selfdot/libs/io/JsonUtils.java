package com.selfdot.libs.io;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
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
            e.printStackTrace(System.err);
        }
    }

    public static <T> void save(Gson gson, T object, String filename) {
        createDirectories(Path.of(filename).getParent());
        try (FileWriter writer = new FileWriter(filename, StandardCharsets.UTF_8)) {
            gson.toJson(object, writer);
            writer.flush();
            log.info("Saved {}", filename);

        } catch (IOException e) {
            log.error("Could not save {}", filename);
            e.printStackTrace(System.err);
        }
    }

    private static <T> T loadWithDefault(Gson gson, String filename, TypeDef<T> typeDef, T default_) {
        T object = default_;
        boolean shouldSave = true;
        createDirectories(Path.of(filename).getParent());
        try (FileReader reader = new FileReader(filename, StandardCharsets.UTF_8)) {
            object = typeDef.fromJson(gson, reader);
            log.info("Loaded {}", filename);
        } catch (FileNotFoundException e) {
            log.warn("{} not found", filename);
        } catch (IOException | JsonSyntaxException e) {
            shouldSave = false;
            log.error("Could not load {}", filename);
            e.printStackTrace(System.err);
        }
        if (shouldSave) save(gson, object, filename);
        if (object == default_) log.warn("Using default object as substitute for {}", filename);
        return object;
    }

    public static <T> T loadWithDefault(Gson gson, String filename, Class<T> clazz, T fallback) {
        return loadWithDefault(gson, filename, new TypeDef<>(clazz), fallback);
    }

    public static <T> T loadWithDefault(Gson gson, String filename, Type type, T fallback) {
        return loadWithDefault(gson, filename, new TypeDef<>(type), fallback);
    }

}
