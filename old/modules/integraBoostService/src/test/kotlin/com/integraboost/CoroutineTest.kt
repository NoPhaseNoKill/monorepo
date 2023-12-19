package com.integraboost

import kotlinx.coroutines.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import kotlin.coroutines.ContinuationInterceptor
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext


class CoroutineTest {

    /**
     *      A fundamental misunderstanding I had around coroutines was how coroutine builders worked
     *      in the context of a runBlocking scope. When inside runBlocking, if you have a suspend function
     *      where the immediate parent's coroutine scope is the runBlocking coroutine scope, this code will run synchronously
     *      and blocks the main thread.
     *
     *      Key takeaways:
     *      1. A function with the 'suspend' modifier is NOT necessarily an async function. It simply allows the function
     *          to be invoked in the scope of a coroutine (which you may or may not be utilizing)
     *
     *      2. When a function meets the below criteria:
     *          i) has 'suspend' modifier
     *          ii) has code that COULD be done async (network request, delays for long-polling etc)
     *          iii) is not wrapped with its own coroutineScope
     *          iv) has an immediate parent of runBlocking
     *
     *      --------> the code will be run synchronously, where it will block the main thread when it runs the code that could have been async
     *
     *      3. When fixing issues like this, you have multiple ways of fixing the issue, depending on the exact sequence
     *         you want to happen. This means you need to be VERY careful about your intentions, as subtle nuances (showcased below)
     *         may result in unexpected behaviour.
     *
     *      4. There is also an option you can choose, whereby the developer may choose to wrap
     *         the synchronous code with another coroutineScope (and get the desired behaviour they were after), HOWEVER,
     *         this spawns an additional unnecessary coroutineScope. The likely impact of this in a production project
     *         for even a medium-sized project is very minimal, but should be noted. Even if you spawn an extra 50,000
     *         coroutines, unless you have child coroutine code which exponentially scales the number of coroutine created,
     *         (turning 50k to 250k extra coroutines for example) you should barely notice this. The real
     *         performance degradation comes if this synchronous code does external network calls, and you start
     *         spamming endpoints and bring down your whole environment :D..
     *
     *      ***WRONG - HOW I HAD ORIGINALLY WRITTEN IT***
     *
     *      response.addWithDelay(SomeDomainObject("will run second", 2))  <------- this actually blocks for 3000ms
     *      response.addWithNoDelay(SomeDomainObject("will run first", 1))  <------ before it gets here
     *
     *      OUTPUTS
     *      [test-log] [2023-10-28T02:09:14.117697] addWithDelay: initial order: []
     *      [test-log] [2023-10-28T02:09:14.123529] addWithDelay: about to simulate async call that takes 3 seconds.....
     *      [test-log] [2023-10-28T02:09:17.128800] addWithDelay: delay should now be finished.
     *      [test-log] [2023-10-28T02:09:17.140148] addWithDelay: final order [SomeDomainObject(message=will run second, expectedToRunAtIndex=2)]
     *      [test-log] [2023-10-28T02:09:17.140694] addWithNoDelay: initial order: [SomeDomainObject(message=will run second, expectedToRunAtIndex=2)]
     *      [test-log] [2023-10-28T02:09:17.140917] addWithNoDelay: final order [SomeDomainObject(message=will run second, expectedToRunAtIndex=2), SomeDomainObject(message=will run first, expectedToRunAtIndex=1)]
     *
     *
     *      ***OPTIONS I COULD HAVE CHOSEN - Option 1***
     *
     *      async {
     *           response.addWithDelay(SomeDomainObject("will run second", 2))
     *      }
     *      response.addWithNoDelay(SomeDomainObject("will run first", 1))
     *
     *      OUTPUTS
     *      [test-log] [2023-10-28T02:15:42.847358] addWithNoDelay: initial order: []
     *      [test-log] [2023-10-28T02:15:42.863743] addWithNoDelay: final order [SomeDomainObject(message=will run first, expectedToRunAtIndex=1)]
     *      [test-log] [2023-10-28T02:15:42.867141] addWithDelay: initial order: [SomeDomainObject(message=will run first, expectedToRunAtIndex=1)]
     *      [test-log] [2023-10-28T02:15:42.867231] addWithDelay: about to simulate async call that takes 3 seconds.....
     *      [test-log] [2023-10-28T02:15:45.873764] addWithDelay: delay should now be finished.
     *      [test-log] [2023-10-28T02:15:45.874916] addWithDelay: final order [SomeDomainObject(message=will run first, expectedToRunAtIndex=1), SomeDomainObject(message=will run second, expectedToRunAtIndex=2)]
     *
     *      ***OPTIONS I COULD HAVE CHOSEN - Option 2***
     *
     *      async {
     *           response.addWithDelay(SomeDomainObject("will run second", 2))
     *      }
     *      // spawning another coroutine here is inefficient but will still get us the same end result, and is to simply showcase how else we could have done it other than option 1
     *      coroutineScope {
     *           response.addWithNoDelay(SomeDomainObject("will run first", 1))
     *      }
     *
     *
     *      OUTPUTS
     *      [test-log] [2023-10-28T02:15:42.847358] addWithNoDelay: initial order: []
     *      [test-log] [2023-10-28T02:15:42.863743] addWithNoDelay: final order [SomeDomainObject(message=will run first, expectedToRunAtIndex=1)]
     *      [test-log] [2023-10-28T02:15:42.867141] addWithDelay: initial order: [SomeDomainObject(message=will run first, expectedToRunAtIndex=1)]
     *      [test-log] [2023-10-28T02:15:42.867231] addWithDelay: about to simulate async call that takes 3 seconds.....
     *      [test-log] [2023-10-28T02:15:45.873764] addWithDelay: delay should now be finished.
     *      [test-log] [2023-10-28T02:15:45.874916] addWithDelay: final order [SomeDomainObject(message=will run first, expectedToRunAtIndex=1), SomeDomainObject(message=will run second, expectedToRunAtIndex=2)]
     *
     *      ***OPTIONS I COULD HAVE CHOSEN - Option 3***
     *
     *      async {
     *           response.addWithDelay(SomeDomainObject("will run second", 2))
     *      }
     *      launch {
     *          response.addWithNoDelay(SomeDomainObject("will run first", 1))
     *      }
     *
     *      OUTPUTS
     *      [test-log] [2023-10-28T02:19:11.242118] addWithDelay: initial order: []
     *      [test-log] [2023-10-28T02:19:11.251235] addWithDelay: about to simulate async call that takes 3 seconds.....
     *      [test-log] [2023-10-28T02:19:11.257128] addWithNoDelay: initial order: []
     *      [test-log] [2023-10-28T02:19:11.269760] addWithNoDelay: final order [SomeDomainObject(message=will run first, expectedToRunAtIndex=1)]
     *      [test-log] [2023-10-28T02:19:14.255993] addWithDelay: delay should now be finished.
     *      [test-log] [2023-10-28T02:19:14.257392] addWithDelay: final order [SomeDomainObject(message=will run first, expectedToRunAtIndex=1), SomeDomainObject(message=will run second, expectedToRunAtIndex=2)]
     */

    @Test
    fun `should be able to understand differences in behaviour between a runBlocking's child coroutine scopes depending on, IF and HOW, they are implemented`(): Unit = runBlocking {
        val response = TestResponse()

        // There are multiple options here, this is just one of the working ones
        launch {
            response.addWithDelay(SomeDomainObject("will run second", 2))
        }
        response.addWithNoDelay(SomeDomainObject("will run first", 1))

//        DO NOT USE THIS UNLESS YOU WANT TO SEE THE BEHAVIOUR I HAD ORIGINALLY IMPLEMENTED (WHICH WAS WRONG)
//        response.addWithDelay(SomeDomainObject("will run second", 2))
//        response.addWithNoDelay(SomeDomainObject("will run first", 1))
    }

    @Test
    fun `should simulate hitting endpoint with code that is using code that could be better be better optimized by using async`() = runBlocking {
        val response = TestResponse()
        val FLEXIBLE_TIMEOUT_FOR_TWO_AND_A_HALF_SECONDS = 2700L

        val result = withTimeoutOrNull(FLEXIBLE_TIMEOUT_FOR_TWO_AND_A_HALF_SECONDS) {
            response.simulateCallingResourceEndpoint()
        }

        Assertions.assertNotNull(result)
    }
}

data class SomeDomainObject(
    val message: String,
    val expectedToRunAtIndex: Int,
)

class TestResponse {

    private var order: List<SomeDomainObject> = emptyList()

    fun addWithNoDelay(domainObject: SomeDomainObject) {
        Log.print("add: initial order: $order")
        order = order.plus(domainObject)
        Log.print("add: final order $order")
    }

    suspend fun addWithDelay(domainObject: SomeDomainObject) {
        Log.print("addWithDelay: initial order: $order")
        Log.print("addWithDelay: about to simulate async call that takes 3 seconds.....")
        delay(3000)
        Log.print("addWithDelay: delay should now be finished.")
        order = order.plus(domainObject)
        Log.print("addWithDelay: final order $order")
    }

    /*
        should take just over 2500ms = 1000 + 1000 + 500
    */
    suspend fun simulateCallingResourceEndpoint() {
        measureTimeAndLog {
            withContext(Dispatchers.Default) {
                Log.print("Simulate coming into resource")
                measureTimeAndLog {

                    measureTimeAndLog {
                        Log.print("Task blocking task 1 start")
                        delay(1000)
                        Log.print("Task blocking task 1 completed")
                    }

                    measureTimeAndLog {
                        Log.print("Task 1 start")
                        delay(1000)
                        Log.print("Task 1 completed")
                    }

                    measureTimeAndLog {
                        launch {
                            measureTimeAndLog {
                                Log.print("Task 2 start")
                                delay(100)
                                Log.print("Task 2 completed")
                            }
                        }
                        launch {
                            measureTimeAndLog {
                                Log.print("Task 3 start")
                                delay(50)
                                Log.print("Task 3 completed")
                            }
                        }

                        val result = async(start = CoroutineStart.LAZY) {
                            measureTimeAndLog {
                                Log.print("Task 4 start")
                                delay(500)
                                Log.print("Task 4 completed")
                            }
                        }
                        Log.print("Task 4 result is: ${result.await()}")
                    }
                }
            }
            Log.print("Simulate exiting resource")
        }
    }
}

object Log {
    fun print(message: String) {
        val time = LocalDateTime.now()
        println("[test-log] [${time}] [${Thread.currentThread().name}] $message")
    }
}

suspend fun <T> measureTimeAndLog(
    context: CoroutineContext = EmptyCoroutineContext,
    block: suspend CoroutineScope.() -> T
): T {
    return coroutineScope {
        val launchContext =
            if (context == EmptyCoroutineContext) coroutineContext[ContinuationInterceptor] as CoroutineDispatcher
            else context

        val start = System.currentTimeMillis()
        Log.print("Task starting")
        val result = block()
        val end = System.currentTimeMillis()
        Log.print("Task finished")
        Log.print("Elapsed time for task: ${end - start} ms. Launch context: $launchContext")
        return@coroutineScope result
    }
}




