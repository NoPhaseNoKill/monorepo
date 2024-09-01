plugins {
    
    id("com.nophasenokill.kotlin-library-plugin")
}

evaluationDependsOnChildren()

println("Depth is: ${getDepth()}")
// depthCompare(project(projects.jvm.standaloneProjects.libraries.someNewLib))
