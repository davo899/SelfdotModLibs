package com.selfdot.libs.io;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class JsonRegistry<T> {

    protected Gson gson = new Gson();
    private final Class<T> clazz;
    private final Path directoryPath;
    protected final Map<String, T> items = new HashMap<>();

    public JsonRegistry(Class<T> clazz, String directoryPathString) {
        this.clazz = clazz;
        this.directoryPath = Paths.get(directoryPathString);
    }

    private void createDirectories() {
        try {
            if (!Files.exists(directoryPath)) Files.createDirectories(directoryPath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        items.clear();

        createDirectories();
        try (Stream<Path> paths = Files.walk(directoryPath)) {
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
                        e.printStackTrace();
                    }
                });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        createDirectories();
        for (Map.Entry<String, T> entry : items.entrySet()) {
            try (FileWriter writer = new FileWriter(directoryPath.resolve(entry.getKey() + ".json").toFile())) {
                gson.toJson(entry.getValue(), writer);
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
