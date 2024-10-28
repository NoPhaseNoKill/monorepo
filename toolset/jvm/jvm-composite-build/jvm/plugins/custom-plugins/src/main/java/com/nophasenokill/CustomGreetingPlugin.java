package com.nophasenokill;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class CustomGreetingPlugin implements Plugin<Project> {

    @Override
    public void apply(Project target) {
        target.getTasks().register("helloCustomPlugin", CustomGreetingTask.class);
        String name = target.getName();
        System.out.println("Applying CustomGreetingPlugin for name: " + name);
    }
}
