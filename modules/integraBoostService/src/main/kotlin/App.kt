class App {
    val greeting: String
        get() = "Hello World!"

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            println(App().greeting)
        }
    }
}