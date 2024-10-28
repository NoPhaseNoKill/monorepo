package com.nophasenokill;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class NonMetaGreetingPlugin implements Plugin<Project> {

    @Override
    public void apply(Project target)
    {
        target.getTasks().register("helloNonMetaPlugin", NonMetaGreetingTask.class);
        String name = target.getName();
        System.out.println("Applying NonMetaGreetingPlugin for name: " + name);
    }
}
