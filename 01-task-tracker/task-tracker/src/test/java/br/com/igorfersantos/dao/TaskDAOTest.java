package br.com.igorfersantos.dao;

import br.com.igorfersantos.Database;
import br.com.igorfersantos.domain.task.Status;
import br.com.igorfersantos.domain.task.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskDAOTest {

    @Mock
    private Database database;

    private TaskDAO taskDAO;

    @BeforeEach
    void setUp() {
        taskDAO = new TaskDAO(database);
    }

    @Test
    void create_shouldCreateTasksWithIncrementalIDs() {
        when(database.getDatabaseTasks()).thenReturn(new java.util.ArrayList<>());

        Task task1 = taskDAO.create("First task");
        Task task2 = taskDAO.create("Second task");

        assertEquals(0, task1.getId());
        assertEquals(1, task2.getId());

        assertEquals("First task", task1.getDescription());
        assertEquals("Second task", task2.getDescription());
        assertEquals(Status.TODO, task1.getStatus());
        assertEquals(Status.TODO, task2.getStatus());
        assertNotNull(task1.getCreatedAt());
        assertNotNull(task2.getCreatedAt());
        assertNull(task1.getUpdatedAt());
        assertNull(task2.getUpdatedAt());

        verify(database, times(4)).getDatabaseTasks();
    }

    @Test
    void findById_shouldReturnTask_whenFound() {
        Task expectedTask = new Task(
                1,
                "Test task",
                Status.TODO,
                LocalDateTime.now(),
                null
        );
        when(database.getDatabaseTasks()).thenReturn(
                new java.util.ArrayList<>(List.of(expectedTask))
        );
        Optional<Task> foundTask = taskDAO.findById(1);

        assertTrue(foundTask.isPresent());
        assertEquals(expectedTask, foundTask.get());
    }

    @Test
    void findById_shouldBeEmpty_whenNotFound() {
        when(database.getDatabaseTasks()).thenReturn(new java.util.ArrayList<>());
        Optional<Task> foundTask = taskDAO.findById(999);

        assertTrue(foundTask.isEmpty());
    }

    @Test
    void findByStatus_shouldReturnFilteredTasks() {
        Task todoTask = new Task("Todo task", Status.TODO);
        Task todoTask2 = new Task("Todo task 2", Status.TODO);
        Task todoTask3 = new Task("Todo task 3", Status.TODO);
        Task inProgressTask = new Task("inProgress Task 1", Status.INPROGRESS);
        Task inProgressTask2 = new Task("inProgress Task 2", Status.INPROGRESS);
        Task doneTask = new Task("Done Task 1", Status.DONE);
        Task doneTask2 = new Task("Done Task 2", Status.DONE);
        Task doneTask3 = new Task("Done Task 2", Status.DONE);
        Task doneTask4 = new Task("Done Task 2", Status.DONE);

        when(database.getDatabaseTasks()).thenReturn(
                new java.util.ArrayList<>(List.of(todoTask,
                        todoTask2,
                        todoTask3,
                        inProgressTask,
                        inProgressTask2,
                        doneTask,
                        doneTask2,
                        doneTask3,
                        doneTask4))
        );

        List<Task> todoTasks = taskDAO.findByStatus(Status.TODO);
        List<Task> inProgressTasks = taskDAO.findByStatus(Status.INPROGRESS);
        List<Task> doneTasks = taskDAO.findByStatus(Status.DONE);
        List<Task> allTasks = taskDAO.findAll();

        assertEquals(3, todoTasks.size());
        assertEquals(Status.TODO, todoTasks.getFirst().getStatus());

        assertEquals(2, inProgressTasks.size());
        assertEquals(Status.INPROGRESS, inProgressTasks.getFirst().getStatus());

        assertEquals(4, doneTasks.size());
        assertEquals(Status.DONE, doneTasks.getFirst().getStatus());

        assertEquals(9, allTasks.size());
    }

    @Test
    void update_shouldUpdateTaskDescription() {
        Task task = new Task(1, "Old description", Status.TODO, LocalDateTime.now(), null);
        when(database.getDatabaseTasks()).thenReturn(new java.util.ArrayList<>(List.of(task)));

        boolean result = taskDAO.update(1, "New description");

        assertTrue(result);
        assertEquals("New description", task.getDescription());
        assertNotNull(task.getUpdatedAt());
    }

    @Test
    void update_shouldReturnFalse_whenTaskNotFound() {
        when(database.getDatabaseTasks()).thenReturn(new java.util.ArrayList<>());

        boolean result = taskDAO.update(999, "New description");

        assertFalse(result);
    }

    @Test
    void delete_shouldRemoveTask() {
        Task task = new Task(1, "Task to delete", Status.TODO, LocalDateTime.now(), null);
        List<Task> tasks = new java.util.ArrayList<>(List.of(task));
        when(database.getDatabaseTasks()).thenReturn(tasks);

        boolean result = taskDAO.delete(1);

        assertTrue(result);
        assertTrue(tasks.isEmpty());
    }

    @Test
    void delete_shouldReturnFalse_whenTaskNotFound() {
        when(database.getDatabaseTasks()).thenReturn(new java.util.ArrayList<>());

        boolean result = taskDAO.delete(999);

        assertFalse(result);
    }
}
