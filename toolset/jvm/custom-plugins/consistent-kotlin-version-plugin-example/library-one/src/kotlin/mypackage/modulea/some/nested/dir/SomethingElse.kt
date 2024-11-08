package mypackage.modulea.some.nested.dir

import mypackage.modulea.ModuleA
import mypackage.modulea.SomeKotlinFile

object SomethingElse {
    fun doSomethingElse(): String {
        val value = ModuleA().doWork()
        println("Getting java value of: $value inside of kotlin file")
        val kotlinValue = SomeKotlinFile.doSomething()
        val javaValue = ModuleA().doWork()
        return javaValue + kotlinValue
    }
}
