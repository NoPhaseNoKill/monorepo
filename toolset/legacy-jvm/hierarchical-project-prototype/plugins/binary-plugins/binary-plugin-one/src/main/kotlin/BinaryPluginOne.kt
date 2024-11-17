import org.gradle.api.Plugin
import org.gradle.api.Project


class BinaryPluginOne: Plugin<Project> {
    override fun apply(project: Project) {
        project.run {
            println("Applying binary plugin one")
        }
    }
}
