package com.selfdot.libs.io;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import lombok.extern.slf4j.Slf4j;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.selfdot.libs.io.JsonUtils.createDirectories;

@Slf4j
public class JsonRegistry<T> {

    protected Gson gson = new Gson();
    private final Class<T> clazz;
    private final Path directoryPath;
    protected final Map<String, T> items = new HashMap<>();

    public JsonRegistry(Class<T> clazz, String directoryPathString) {
        this.clazz = clazz;
        this.directoryPath = Paths.get(directoryPathString);
    }

    protected boolean validateKey(String key) {
        return true;
    }

    protected boolean validate(T item) {
        return true;
    }

    public void load() {
        items.clear();

        createDirectories(directoryPath);
        try (Stream<Path> paths = Files.walk(directoryPath)) {
            List<String> invalidItems = new ArrayList<>();
            paths.filter(Files::isRegularFile)
                .filter(path -> path.toString().endsWith(".json"))
                .forEach(path -> {
                    try (FileReader reader = new FileReader(path.toFile(), StandardCharsets.UTF_8)) {
                        String fileName = path.getFileName().toString();
                        int dotIndex = fileName.lastIndexOf('.');
                        items.put(
                            dotIndex == -1 ? fileName : fileName.substring(0, dotIndex),
                            gson.fromJson(reader, clazz)
                        );
                    } catch (IOException | JsonSyntaxException e) {
                        invalidItems.add(path.getFileName().toString());
                        e.printStackTrace();
                    }
                });

            for (String key : items.keySet()) {
                if (!validateKey(key) || !validate(items.get(key))) invalidItems.add(key);
            }
            invalidItems.forEach(key -> {
                log.error("Skipping invalid " + clazz.getSimpleName() + ": " + key);
                items.remove(key);
            });
            log.info(
                "Finished loading " + clazz.getSimpleName() + " registry: " + items.size() +
                " valid, " + invalidItems.size() + " invalid"
            );

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save(String key) {
        T item = items.get(key);
        if (item == null) return;
        createDirectories(directoryPath);
        try (FileWriter writer = new FileWriter(directoryPath.resolve(key + ".json").toFile())) {
            gson.toJson(item, writer);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        items.keySet().forEach(this::save);
    }

}
