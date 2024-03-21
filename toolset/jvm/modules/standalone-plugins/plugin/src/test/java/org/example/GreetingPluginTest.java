package org.example;


import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;


public class GreetingPluginTest {

    @Test
    public void greeterPluginAddsGreetingTaskToProject() {
        Project project = ProjectBuilder.builder().build();
        project.getPluginManager().apply(GreetingPlugin.class);

        Task task = project.getTasks().findByName("hello");

        assertNotNull(task, "Task 'hello' should not be null");
    }
}


