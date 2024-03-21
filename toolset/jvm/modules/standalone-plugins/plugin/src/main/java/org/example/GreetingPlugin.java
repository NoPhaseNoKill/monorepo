package org.example;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class GreetingPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        // Register the task and apply an action in one step
        project.getTasks().register("hello", task ->
                task.doLast(t ->
                        System.out.println("Hello from the GreetingPlugin")
                )
        );
    }
}