package br.com.igorfersantos.dao;

import br.com.igorfersantos.Database;
import br.com.igorfersantos.domain.task.Status;
import br.com.igorfersantos.domain.task.Task;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class TaskDAO {

    private final ObjectMapper objectMapper;
    private final Database database;

    public TaskDAO(Database database) {
        objectMapper = new ObjectMapper();
        this.database = database;
    }

    public Task create(String description) {
        final Task lastTask;
        int newId = 0;
        try {
            lastTask = database.getDatabaseTasks().getLast();
            newId = lastTask.getId() + 1;
        } catch (NoSuchElementException ignored) {
        }

        Task newTask = new Task(
                newId,
                description,
                Status.TODO,
                LocalDateTime.now(),
                null
        );
        database.getDatabaseTasks().add(newTask);
        return newTask;
    }

    public Optional<Task> findById(int id) {
        return database.getDatabaseTasks()
                .stream()
                .filter(task -> task.getId() == id)
                .findFirst();
    }

    public List<Task> findByStatus(Status status) {
        return database.getDatabaseTasks().stream()
                .filter(t -> t.getStatus().equals(status))
                .toList();
    }

    public List<Task> findAll() {
        return database.getDatabaseTasks();
    }

    public boolean update(int id, String newDescription) {
        final Optional<Task> task = findAll().stream().filter(t -> t.getId() == id).findFirst();
        if (task.isEmpty()) {
            return false;
        }
        task.get().setDescription(newDescription);
        task.get().setUpdatedAt(LocalDateTime.now());
        return true;
    }

    public boolean delete(int id) {
        final Optional<Task> task = findAll().stream().filter(t -> t.getId() == id).findFirst();
        return task.map(value -> findAll().remove(value)).orElse(false);
    }
}
