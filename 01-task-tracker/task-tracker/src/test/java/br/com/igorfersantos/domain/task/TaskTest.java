package br.com.igorfersantos.domain.task;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    public void givenTask_whenTestingHashCodeConsistency_thenConsistentHashCodeReturned() {
        Task task = new Task();
        task.setId(0);
        task.setDescription("A task");
        int hashCode1 = task.hashCode();
        int hashCode2 = task.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void givenTwoEqualObjects_whenTestingHashCodeEquality_thenEqualHashCodesReturned() {
        Task task1 = new Task();
        task1.setId(0);
        task1.setDescription("A very important task");
        Task task2 = new Task();
        task2.setId(0);
        task2.setDescription("A very important task");

        assertEquals(task1.hashCode(), task2.hashCode());
    }

    @Test
    public void givenMultipleTasks_whenTestingHashCodeDistribution_thenEvenDistributionOfHashCodes() {
        List<Task> tasks = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            Task task = new Task();
            task.setId(i);
            task.setDescription("Task " + 1);
            tasks.add(task);
        }
        Set<Integer> hashCodes = new HashSet<>();
        for (Task task: tasks) {
            hashCodes.add(task.hashCode());
        }
        assertEquals(tasks.size(), hashCodes.size(), 10);
    }
}