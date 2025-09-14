package br.com.igorfersantos.dao;

import br.com.igorfersantos.Database;
import br.com.igorfersantos.RetrieveStrategy;
import br.com.igorfersantos.domain.task.Status;
import br.com.igorfersantos.domain.task.Task;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.Objects.isNull;

public class TaskDAO {

    private final ObjectMapper objectMapper;
    private final Database database;

    public TaskDAO(Database database) {
        objectMapper = new ObjectMapper();
        this.database = database;
    }

    public Task create(String description) {
        final Task lastTask = database.getDatabaseTasks().getLast();
        int newId = 0;
        if (!isNull(lastTask)) {
            newId = lastTask.getId() + 1;
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

    public Task findById(int id) {
        return null;
    }

    public List<Task> findByStrategy(RetrieveStrategy retrieveStrategy) {
        return null;
    }

    public boolean update(int id, String newDescription) {
        return false;
    }

    public boolean delete(int id) {
        return false;
    }

    public boolean persistDatabase() {
        return false;
    }
}
