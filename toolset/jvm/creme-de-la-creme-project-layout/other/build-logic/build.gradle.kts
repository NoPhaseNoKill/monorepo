plugins {
   id("groovy-gradle-plugin")

}

println("Hello from build ${file(".")}")

apply(plugin = "base")
println("Hello from settings v2 ${file(".")}")
