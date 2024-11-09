package org.sample;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import tasks.CompareBinaryDataTask;
import tasks.InspectRocksDBTask;
import tasks.StoreBinaryDataInCacheTask;


/**
 * A custom Gradle plugin for number utilities. This is the project
 *  plugin (build.gradle.kts).
 */
public class NumberUtilsPlugin implements Plugin<Project> {

    /**
     * Default constructor
     */
    public NumberUtilsPlugin() {
    }

    @Override
    public void apply(Project project) {

        System.out.println("Applying NumberUtilsPlugin");

        project.getTasks().register("storeBinaryDataInCache", StoreBinaryDataInCacheTask.class);
        project.getTasks().register("compareBinaryData", CompareBinaryDataTask.class).configure( task -> task.dependsOn("storeBinaryDataInCache"));
        project.getTasks().register("inspectRocksDB", InspectRocksDBTask.class).configure(task -> task.dependsOn("compareBinaryData"));
    }
}
