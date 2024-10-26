object UsesRootBuildFile {

    fun useFile(): String {
        val result = FromRootBuild.doSomething()
        assert(result == "Ohai")

        return "Ohai2"
    }
}
