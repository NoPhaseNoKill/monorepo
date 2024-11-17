import org.gradle.api.Plugin
import org.gradle.api.Project

class BinaryPluginTwo: Plugin<Project> {
    override fun apply(project: Project) {
        project.run {
            println("Applying binary plugin two")
        }
    }
}
