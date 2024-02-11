import com.nophasenokill.CreateMD5


plugins {
    id("base-plugin")
}

/*
    Task which is used to showcase parallelism that gradle offers through the worker API.
    It allows you to split up tasks into units of work, which can run in/on their own thread
    and allows you to run tasks fully async.
 */
tasks.register<CreateMD5>("sourceFileHashingPluginTask") {
    destinationDirectory = project.layout.buildDirectory.dir("sourceFileHashingPluginTask")
    source(project.layout.projectDirectory.file("src"))
}

