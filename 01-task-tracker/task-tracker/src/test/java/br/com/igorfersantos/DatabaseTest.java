package br.com.igorfersantos;

import br.com.igorfersantos.domain.task.Status;
import br.com.igorfersantos.domain.task.Task;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DatabaseTest {

    @Spy
    static Database database;
    static File databaseFile;
    static String databaseExampleDataFilename = "/databaseExample.json";
    static String databaseExampleData;

    @BeforeAll
    static void setup() throws IOException, URISyntaxException {
        database = new Database();
        databaseFile = new File("tasks.json");

        final URL databaseResourceURL = DatabaseTest.class.getResource(
                databaseExampleDataFilename
        );
        databaseExampleData = Files.readString(
                Path.of(databaseResourceURL.toURI())
        );

        Files.deleteIfExists(databaseFile.toPath());
    }

    @AfterAll
    static void teardown() throws IOException {
        Files.deleteIfExists(databaseFile.toPath());
    }

    @Test
    @Order(1)
    void testDatabaseExists_shouldBeFalse_whenDatabaseDontExists() {
        boolean databaseExists = database.checkDatabaseExists();
        assertFalse(databaseExists);
    }

    @Test
    @Order(2)
    void initializeDatabase_shouldCreateValidDatabaseFile_whenDatabaseDoesNotExist()
            throws IOException {
        if(Files.exists(databaseFile.toPath()))
            fail("Database file should not exist yet");

        database.initializeDatabase();
        final boolean fileExists = Files.exists(databaseFile.toPath());

        assertDoesNotThrow(() ->
                Files.writeString(
                        databaseFile.toPath(),
                        databaseExampleData,
                        StandardOpenOption.WRITE
                )
        );
        assertTrue(fileExists);
        assertTrue(Files.size(databaseFile.toPath()) > 0);
    }

    @Test
    @Order(3)
    void loadDatabase_shouldPopulateTheDatabase_whenNotLoadedYet() {
        if (database.getDatabaseTasks() != null)
            fail("Should not be loaded yet");

        database.loadDatabase();
        final List<Task> tasks = database.getDatabaseTasks();

        Task t1 = new Task(
                1,
                "Finish writing Task Tracker CLI project",
                Status.INPROGRESS,
                LocalDateTime.ofInstant(Instant.parse("2025-09-13T18:30:00Z"), ZoneOffset.UTC),
                LocalDateTime.ofInstant(Instant.parse("2025-09-13T19:00:00Z"), ZoneOffset.UTC)
        );
        Task t2 = new Task(
                2,
                "Read documentation on file system modules",
                Status.DONE,
                LocalDateTime.ofInstant(Instant.parse("2025-09-12T14:20:00Z"), ZoneOffset.UTC),
                LocalDateTime.ofInstant(Instant.parse("2025-09-12T16:10:00Z"), ZoneOffset.UTC)
        );
        Task t3 = new Task(
                3,
                "Buy groceries",
                Status.TODO,
                LocalDateTime.ofInstant(Instant.parse("2025-09-13T12:05:00Z"), ZoneOffset.UTC),
                LocalDateTime.ofInstant(Instant.parse("2025-09-13T12:05:00Z"), ZoneOffset.UTC)
        );

        List<Task> tasksToCompare = new ArrayList<>(List.of(t1, t2, t3));

        assertEquals(3, tasks.size());
        assertEquals(tasks, tasksToCompare);
    }

    @Order(4)
    @Test
    void persistDatabase_shouldProduceAValidDatabaseFile_whenDatabaseIsPersisted() {
        Task task = new Task("Test my java program", Status.TODO);
        // TODO: Integrade TaskDAO to create the task here later
        database.getDatabaseTasks().add(task);
        assertTrue(database.persistDatabase());
        // Clear so we can load again to check if it was correctly persisted
        database.getDatabaseTasks().clear();
        database.loadDatabase();

        List<Task> tasks = database.getDatabaseTasks();
        assertEquals(4, tasks.size());
    }

    @Order(5)
    @Test
    void persistDatabase_shouldHaveMaintainedThePreviousTasks_whenDatabaseWasPersisted() {
        List<Task> tasks = database.getDatabaseTasks();

        Task t1 = new Task(
                1,
                "Finish writing Task Tracker CLI project",
                Status.INPROGRESS,
                LocalDateTime.ofInstant(Instant.parse("2025-09-13T18:30:00Z"), ZoneOffset.UTC),
                LocalDateTime.ofInstant(Instant.parse("2025-09-13T19:00:00Z"), ZoneOffset.UTC)
        );
        Task t2 = new Task(
                2,
                "Read documentation on file system modules",
                Status.DONE,
                LocalDateTime.ofInstant(Instant.parse("2025-09-12T14:20:00Z"), ZoneOffset.UTC),
                LocalDateTime.ofInstant(Instant.parse("2025-09-12T16:10:00Z"), ZoneOffset.UTC)
        );
        Task t3 = new Task(
                3,
                "Buy groceries",
                Status.TODO,
                LocalDateTime.ofInstant(Instant.parse("2025-09-13T12:05:00Z"), ZoneOffset.UTC),
                LocalDateTime.ofInstant(Instant.parse("2025-09-13T12:05:00Z"), ZoneOffset.UTC)
        );

        List<Task> tasksToCompareTo = new ArrayList<>(List.of(t1,t2,t3));

        for (int i = 0; i <= 2; i++) {
           assertEquals(tasks.get(i), tasksToCompareTo.get(i));
        }
    }

    @Test
    @Order(6)
    void getDatabase() {
    }

}