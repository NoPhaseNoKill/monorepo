plugins {
    id("com.nophasenokill.kotlin-application-plugin")
}

group = "com.nophasenokill.applications"
version = "0.1.local-dev"

dependencies {
    
}

/*

   The primary reason for this is that by default distTar and distZip
   tasks are not cacheable. While the thread here:
   https://discuss.gradle.org/t/why-are-tar-jar-tasks-not-cacheable-by-default/23116/3
   suggests that savings are milliseconds, when using the '--rerun-tasks' on the buildComposite
   task where nothing has changed, it re-triggers these tasks. This results in anywhere between
   7-10 seconds of unnecessary time. I'm of the opinion that considering the task
   :applications:application-one:classes is UP-TO-DATE in the same scenario, these should be too.

   Example at the time of writing:

        :applications:application-one:distTar
         Started After 1.817s Duration 2.646s Not cacheable: Caching not enabled for work unit
        :applications:application-one:distZip
         Started After 4.463s Duration After 7.840s	Not cacheable: Caching not enabled for work unit

   In future consider something like:

         class CustomTask extends DefaultTask {
            @Input
            File inputFile

            @OutputFile
            File outputFile

            @TaskAction
            void perform() {
                outputFile.text = inputFile.text.reverse()
            }

            outputs.upToDateWhen {
                // Custom logic to determine if the task is up-to-date
                outputFile.exists() && outputFile.text == inputFile.text.reverse()
            }
        }

   Important note: The thread also mentions that gradle may not track inputs such as
   renaming of files, so to ensure that these are tracked, this task includes the
   classes as outputs, but does NOT include them as a dependency.
 */

tasks.distZip {
    outputs.files(getLayout().buildDirectory.asFile.map { it.resolve("distributions/application-one-1.0-SNAPSHOT.zip") })
    outputs.files(tasks.classes.map { it.outputs })
}

tasks.distTar {
    outputs.files(getLayout().buildDirectory.asFile.map { it.resolve("distributions/application-one-1.0-SNAPSHOT.tar") })
    outputs.files(tasks.classes.map { it.outputs })
}

tasks.test {

}