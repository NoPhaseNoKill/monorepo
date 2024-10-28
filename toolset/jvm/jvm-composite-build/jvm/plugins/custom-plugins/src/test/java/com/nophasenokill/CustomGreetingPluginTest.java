package com.nophasenokill;

import org.junit.Test;
import org.gradle.testfixtures.ProjectBuilder;
import org.gradle.api.Project;
import static org.junit.Assert.assertTrue;

public class CustomGreetingPluginTest {
    @Test
    public void greeterPluginAddsGreetingTaskToProject() {
        Project project = ProjectBuilder.builder().build();
        project.getPluginManager().apply("com.nophasenokill.custom-plugins.greeting");

        assertTrue(project.getTasks().getByName("helloCustomPlugin") instanceof CustomGreetingTask);
    }
}
