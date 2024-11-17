
package com.nophasenokill.basics.classanalysis


class ClassGraph(
    private val keepPackages: PackagePatterns,
    private val unshadedPackages: PackagePatterns,
    private val ignorePackages: PackagePatterns,
    shadowPackage: String
) {

    private
    val classes: MutableMap<String, ClassDetails> = linkedMapOf()

    val entryPoints: MutableSet<ClassDetails> = linkedSetOf()

    val shadowPackagePrefix =
        shadowPackage.takeIf(String::isNotEmpty)
            ?.let { it.replace('.', '/') + "/" }
            ?: ""

    operator fun get(className: String) =
        classes.computeIfAbsent(className) {
            val outputClassName = if (unshadedPackages.matches(className)) className else shadowPackagePrefix + className
            ClassDetails(outputClassName).also { classDetails ->
                if (keepPackages.matches(className) && !ignorePackages.matches(className)) {
                    entryPoints.add(classDetails)
                }
            }
        }

    fun getDependencies() = classes.map { it.value.outputClassFilename to it.value.dependencies.map { it.outputClassFilename } }.toMap()
}


class ClassDetails(val outputClassName: String) {
    var visited: Boolean = false
    val dependencies: MutableSet<ClassDetails> = linkedSetOf()
    val outputClassFilename
        get() = "$outputClassName.class"
}


class PackagePatterns(givenPrefixes: Set<String>) {

    private
    val prefixes: MutableSet<String> = hashSetOf()

    private
    val names: MutableSet<String> = hashSetOf()

    init {
        givenPrefixes.map { it.replace('.', '/') }.forEach { internalName ->
            names.add(internalName)
            prefixes.add("$internalName/")
        }
    }

    fun matches(packageName: String): Boolean {
        if (names.contains(packageName)) {
            return true
        }
        for (prefix in prefixes) {
            if (packageName.startsWith(prefix)) {
                names.add(packageName)
                return true
            }
        }
        return false
    }
}
