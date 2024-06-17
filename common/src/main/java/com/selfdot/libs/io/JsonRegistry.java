package com.selfdot.libs.io;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class JsonRegistry<T> {

    private final Gson gson = new Gson();
    private final Class<T> clazz;
    private final String directoryPath;
    protected final Map<String, T> items = new HashMap<>();

    public JsonRegistry(Class<T> clazz, String directoryPath) {
        this.clazz = clazz;
        this.directoryPath = directoryPath;
    }

    public void load() {
        items.clear();

        try (Stream<Path> paths = Files.walk(Paths.get(directoryPath))) {
            paths.filter(Files::isRegularFile)
                .filter(path -> path.toString().endsWith(".json"))
                .forEach(path -> {
                    try (FileReader reader = new FileReader(path.toFile())) {
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

}
