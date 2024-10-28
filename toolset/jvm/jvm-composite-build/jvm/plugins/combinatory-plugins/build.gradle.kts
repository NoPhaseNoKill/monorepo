import com.nophasenokill.CustomGreetingTask
import com.nophasenokill.NonMetaGreetingTask

plugins {
    id("com.nophasenokill.non-meta-plugins.greeting") version "1.0"
    id("com.nophasenokill.custom-plugins.greeting") version "1.0"
}

val customGreetingTask = tasks.register<CustomGreetingTask>("customGreeting") {
    greeting = "howdy!"
}

val nonMetaGreetingTask = tasks.register<NonMetaGreetingTask>("nonMetaGreeting") {
    greeting = "howdy!"
}

tasks.register("greeting") {
    dependsOn(customGreetingTask)
    dependsOn(nonMetaGreetingTask)
}
