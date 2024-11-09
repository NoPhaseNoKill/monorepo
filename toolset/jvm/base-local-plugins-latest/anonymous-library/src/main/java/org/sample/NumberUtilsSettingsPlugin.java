package org.sample;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.initialization.Settings;

/**
 * A custom Gradle plugin for number utilities. This is the project
 * root plugin (settings.gradle.kts) .
 */
public class NumberUtilsSettingsPlugin implements Plugin<Settings> {

    /**
     * Default constructor
     */
    public NumberUtilsSettingsPlugin() {
    }

    @Override
    public void apply(Settings settings) {
        System.out.println("Applying NumberUtilsSettingsPlugin");
    }
}
