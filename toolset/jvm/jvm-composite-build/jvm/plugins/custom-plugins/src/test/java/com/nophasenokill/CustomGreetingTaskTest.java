package com.nophasenokill;

import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertTrue;

public class CustomGreetingTaskTest {
    @Test
    public void canAddTaskToProject() {
        Project project = ProjectBuilder.builder().build();
        Task task = project.task(Collections.singletonMap("type", CustomGreetingTask.class), "name");
        assertTrue(task instanceof CustomGreetingTask);
    }
}
