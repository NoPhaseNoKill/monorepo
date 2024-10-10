import org.gradle.api.Plugin
import org.gradle.api.Project

class JavaBasePlugin: Plugin<Project> {
    override fun apply(project: Project) {
        println("Applying java base plugin")
    }
}
