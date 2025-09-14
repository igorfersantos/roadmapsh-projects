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
    private List<Task> databaseTasks;
    private final ObjectMapper objectMapper;

    public Database() {
        this.objectMapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();
    }

    public List<Task> getDatabaseTasks() {
        return databaseTasks;
    }

    public void initializeDatabase() {
        try {
            Files.createFile(Path.of(databaseFilename));
        } catch (IOException e) {
            System.out.println(
                    "There was an error while trying to create the tasks file: " +
                            e.getMessage());
            System.out.println("The application will not proceed.");
            System.exit(-1);
        }
    }

    public boolean checkDatabaseExists() {
        return Files.exists(Path.of(databaseFilename));
    }

    public boolean persistDatabase() {
        try {
            String jsonData = objectMapper.writeValueAsString(databaseTasks);
            Files.writeString(Path.of(databaseFilename), jsonData, StandardOpenOption.WRITE);
            return true;
        } catch (IOException e) {
            System.out.println("There was an error while trying to persist" +
                    "the tasks to the database: " + e.getMessage());
            System.out.println("Your tasks will not be saved!");
        }
        return false;
    }

    public void loadDatabase() {
        final File databaseFile = new File(databaseFilename);
        try {
            databaseTasks = objectMapper.readValue(databaseFile, 
                objectMapper.getTypeFactory().constructCollectionType(List.class, Task.class));
        } catch (JsonParseException e) {
            System.out.println(
                    "There was an error while parsing the database: " +
                            e.getMessage()
            );
        } catch (IOException e) {
            System.out.println(
                    "There was an error while reading the database: " +
                            e.getMessage()
            );
        }
    }
}
