
tasks.register("shouldNotTrigger") {

    val input = 5 * 10
    val output = file(layout.buildDirectory.file("shouldNotTrigger.txt"))

    inputs.property("someProperty", input)
    outputs.file(output)

    doLast {
        if(!output.exists()) {
            output.createNewFile()
        }

        output.writeText("You should not see this: $input")
    }
}
