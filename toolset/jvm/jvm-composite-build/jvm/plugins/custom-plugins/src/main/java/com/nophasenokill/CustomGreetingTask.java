package com.nophasenokill;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;

public class CustomGreetingTask extends DefaultTask {

    private String greeting = "hello from CustomGreetingTask";

    public void setGreeting(String greeting) {
        this.greeting = greeting;
    }

    @Input
    public String getGreeting() {
        return greeting;
    }

    @TaskAction
    public void greet() {
        System.out.println(greeting);
    }
}
