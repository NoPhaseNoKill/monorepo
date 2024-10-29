package org.sample;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskProvider;

public class GreetingSecondaryPlugin implements Plugin<Project> {

    public void apply(Project project) {
        GreetingExtension extension = project.getExtensions().create("greetingSecondary", GreetingExtension.class);
        extension.getWho().convention("mate");
        TaskProvider<GreetingTask> task = project.getTasks().register("greetingSecondary", GreetingTask.class, t -> {
            t.getWho().convention(extension.getWho());
        });
    }
}
