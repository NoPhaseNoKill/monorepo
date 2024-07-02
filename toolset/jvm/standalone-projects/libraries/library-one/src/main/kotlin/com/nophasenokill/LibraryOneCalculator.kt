package com.nophasenokill

import java.io.File

object LibraryOneCalculator {
    fun add(a: Int, b: Int): Int {
        Log.write("add method called")
        return a + b
    }

    fun subtract(a: Int, b: Int): Int {
        Log.write("subtract method called")
        return a - b
    }

    /*
        Uncomment this to showcase how you might avoid running tests for something unrelated to tests
        that have previously passed. We'll call this: 'The holy grail of incremental feedback'.
     */

    // fun multiply(a: Int, b: Int): Int {
    //     Log.write("multiply method called")
    //     return a * b
    // }
}

/*
    Example of how we might instrument something for our tests,
    which then form our test execution based on the classes that run.

    This could, for example, use something like ByteBuddy, which appends
    an init log message for all com.nophasenokill classes. If we then instrumented
    these at runtime for the tests, we could have a list of classes which formed
    our test execution. We could then use those results, paired with the examination
    of which test class sources have changed - and we would have a fairly accurate
    idea of which specific tests need to be run.

    Example:

        Assuming the class:

            object LibraryOneCalculator {
                fun add(a: Int, b: Int): Int {
                    Log.write("add method called")
                    return a + b
                }

                fun subtract(a: Int, b: Int): Int {
                    Log.write("subtract method called")
                    return a - b
                }
            }

        and assuming this test:

            class LibraryOneCalculatorTest {

                @Test
                fun `should add two numbers together`() {
                    val result = LibraryOneCalculator.add(2, 2)
                    val expected = 4

                    Assertions.assertEquals(expected, result)
                }

                @Test
                fun `should subtract one number from another`() {
                    val result = LibraryOneCalculator.subtract(2, 2)
                    val expected = 0

                    Assertions.assertEquals(expected, result)
                }
            }

        1. For each method/property on a class (LibraryOneCalculator), we add a "Class.method/property was hit during test execution"
        2. When our test suite runs, we log both the 'add' and 'subtract' methods
        3. This is output to a file, which if the tests were run in a consistent order would output:

            LibraryOneCalculator.add was hit during test execution
            LibraryOneCalculator.subtract was hit during test execution

        4. When we then add the following code to the LibraryOneCalculator:

            object LibraryOneCalculator {
                ...

                fun multiply(a: Int, b: Int): Int {
                    Log.write("multiply method called")
                    return a * b
                }
            }

           if we then ran our tests - it would be able to determine that the instrumented classes would not modify
           the test output, which would still be as below (because we haven't added a CALL to this method/anything
           that interacts with it):

                LibraryOneCalculator.add was hit during test execution
                LibraryOneCalculator.subtract was hit during test execution

        5. And hence for the given inputs (the test source classes, and the side effects of their execution), we
            would have the same given outputs (the test execution file)

 */
private object Log {
    val currentLog = "build/current-test-execution.txt"
    val previousLog = "build/previous-test-execution.txt"
    val currentTestExecutionLog = File(currentLog)
    val previousTestExecutionLog = File(previousLog)

    init {
        if(!previousTestExecutionLog.exists()) {
            // First run
            previousTestExecutionLog.createNewFile()
            currentTestExecutionLog.createNewFile()
        } else {
            // Move last run's contents so we can have a comparison across test suite runs
            currentTestExecutionLog.copyTo(previousTestExecutionLog, overwrite = true)
            currentTestExecutionLog.writeText("")
        }
    }

    fun write(message: String) {
        if(currentTestExecutionLog.readLines().isNotEmpty()) {
            currentTestExecutionLog.appendText("\n")
        }

        currentTestExecutionLog.appendText(message)

    }
}