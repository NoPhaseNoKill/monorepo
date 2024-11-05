object SomethingElse {
    fun doSomethingElse(): String {
        val something = ModuleA().doWork()
        println("Calling java function from kotlin. Result was $something")
        return "Something else2"
    }
}
