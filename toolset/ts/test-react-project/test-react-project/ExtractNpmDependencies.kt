import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import java.io.File
import kotlinx.serialization.json.*

object ExtractNpmDependencies {
    @OptIn(ExperimentalSerializationApi::class)
    fun createPackageJsonWithNewDeps(packageLockFile: File, packageJsonFile: File, outputPackageJsonFile: File) {

        val lockDependencies = extractLatestDependenciesFromLockFile(packageLockFile)
        val existingPackageJson = Json.parseToJsonElement(packageJsonFile.readText()).jsonObject

        val updatedPackageJson = buildJsonObject {
            existingPackageJson.forEach { (key, value) ->
                if (key != "dependencies") {
                    put(key, value)
                }
            }

            putJsonObject("dependencies") {
                lockDependencies.forEach { (name, version) ->
                    put(name, version)
                }
            }
        }

        val json = Json {
            prettyPrint = true
            prettyPrintIndent = "  "
        }
        outputPackageJsonFile.writeText(json.encodeToString(JsonObject.serializer(), updatedPackageJson))
    }

    private fun extractLatestDependenciesFromLockFile(packageLockFile: File): Map<String, String> {
        val packageLockJson = Json.parseToJsonElement(packageLockFile.readText()).jsonObject
        val packages = packageLockJson["packages"]?.jsonObject
            ?: throw IllegalArgumentException("No 'packages' field found in package-lock.json")

        return packages.entries
            .asSequence()
            .filter { (key, _) -> key.isNotEmpty() } // Skip root entry
            .mapNotNull { (key, value) ->
                if (value is JsonObject) {

                    val nodeModulesFoundInString = Regex("node_modules/").findAll(key).count()
                    val version = value["version"]?.jsonPrimitive?.content ?: return@mapNotNull null

                    // Skip transitive dependencies with multiple `node_modules` occurrences
                    if (nodeModulesFoundInString > 1) return@mapNotNull null

                    // Don't include the dependency if optional, as it'll be pulled in transitively anyway

                    val isOptional = value["optional"].toString() == "true"

                    if(isOptional) {
                        return@mapNotNull null
                    }

                    val cleanKey = key.replace("node_modules/", "")
                    cleanKey to version

                } else null
            }
            .groupBy({ it.first }, { it.second }) // Group by package name
            .mapValues { (_, versions) ->
                versions.maxByOrNull { it } ?: "unknown" // Use the latest version
            }
    }

    fun extractLastAfterNodeModules(path: String): String {
        val lastIndex = path.lastIndexOf("node_modules/")
        return if (lastIndex != -1) path.substring(lastIndex + "node_modules/".length) else path
    }

    fun extract(packageLockFile: File): String {
        println("Reading package-lock.json from ${packageLockFile.absolutePath}")

        // Parse the file
        val packageLockJson = Json.parseToJsonElement(packageLockFile.readText()).jsonObject
        val packages = packageLockJson["packages"]?.jsonObject
            ?: throw IllegalArgumentException("No 'packages' field found in package-lock.json")

        println("Found 'packages' field with ${packages.size} entries")

        val dependencies = mutableSetOf<String>()

        for ((key, value) in packages) {
            // Skip the root entry (e.g., "") and ensure it's a JsonObject
            if (key.isNotEmpty() && value is JsonObject) {
                val version = value["version"]?.jsonPrimitive?.content ?: "unknown"
                dependencies.add("$key@$version")
            }
        }
        return dependencies
            .map { it.replaceBeforeLast("node_modules", "") }
            .map { it.replace("node_modules/", "") }
            .toSet()
            .sorted()
            .joinToString("\n")

    }

    fun highlightDupes(extractFile: File): String {

        val depsToVersions = extractFile.readText().lines()
            .filter { it.isNotBlank() } // Remove empty lines
            .map { line ->
                val name = line.substringBeforeLast("@") // Part before the last '@'
                val version = line.substringAfterLast("@") // Part after the last '@'
                name to version
            }
            .groupBy({ it.first }, { it.second }) // Group by dependency name, collecting versions
            .filter { it.value.size > 1 }

        return depsToVersions.map { "${it.key}" to it.value }
            .map { "${it.first} ${it.second}" }
            .joinToString("\n")

    }
}