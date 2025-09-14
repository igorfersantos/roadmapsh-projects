package br.com.igorfersantos;

import br.com.igorfersantos.domain.task.Task;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class Database {
    private final String databaseFilename = "tasks.json";
    private final ObjectMapper objectMapper;
    private List<Task> databaseTasks;

    public Database() {
        this.objectMapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();
    }

    public List<Task> getDatabaseTasks() {
        return databaseTasks;
    }

    public void initializeDatabase() throws IOException {
        Files.createFile(Path.of(databaseFilename));

    }

    public boolean checkDatabaseExists() {
        return Files.exists(Path.of(databaseFilename));
    }

    public void persistDatabase() throws IOException {
        String jsonData = objectMapper.writeValueAsString(databaseTasks);
        Files.writeString(Path.of(databaseFilename), jsonData, StandardOpenOption.WRITE);
    }

    public void loadDatabase() throws IOException {
        final File databaseFile = new File(databaseFilename);
        databaseTasks = objectMapper.readValue(databaseFile,
                objectMapper.getTypeFactory().constructCollectionType(List.class, Task.class));
    }
}
