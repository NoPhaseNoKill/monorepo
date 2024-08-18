package com.nophasenokill.components

import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import kotlinx.coroutines.*
import org.gradle.tooling.ProjectConnection

@Composable
fun GradleToolingApiSection(
    modifier: Modifier = Modifier,
    gradleConnector: ProjectConnection,
    task: String,
) {
    var isLoadingContent by remember { mutableStateOf(true) }
    var output by remember { mutableStateOf<String?>(null) }
    val scrollState = rememberScrollState()
    var shouldRunTask by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        println("About to start loading 5000ms....")
        delay(5000)
        println("Loading....")
        isLoadingContent = false
        println("Setting value to: 1")
        output = "1"
    }

    if(!isLoadingContent) {
        TextButton(onClick = {}) {
            Text("Text is: ${output}")
        }
    } else {
        println("Setting value to: 2")
        output = "2"
    }

    println("Setting value to: 3")
    output = "3"



    // if(isLoadingContent) {
    //     println("Loading...")
    //
    //     LaunchedEffect(Unit) {
    //         val result = withContext(Dispatchers.IO) {
    //
    //             suspend fun result (): String {
    //                 println("Delaying 1000ms")
    //                 val awaited = async(this.coroutineContext) {
    //                     println("Delaying waited for 1000ms, returning bob")
    //                     delay(1000)
    //                     output = "bob"
    //                     "bob"
    //                 }
    //                 return awaited.await()
    //             }
    //
    //             val actual = result()
    //             println("Actual result is: ${actual}")
    //
    //             return@withContext actual
    //         }
    //
    //         if(result.isNotEmpty()) {
    //             println("Result is: ${result}")
    //         }
    //     }
    // } else {
    //     Button(onClick = {}, ) {
    //         Text(text = "This should only show when result has been fetched")
    //     }
    // }

    // Column(modifier) {
        // if (isLoadingContent) {
            // LaunchedEffect(shouldRunTask) {
            //     if (shouldRunTask) {
            //         isLoadingContent = true
            //         output = ""
            //
            //         val result = withContext(Dispatchers.IO) {
            //             GradleToolingApi.runTask(gradleConnector, task)
            //         }
            //
            //         output = result
            //         isLoadingContent = false
            //         shouldRunTask = false
            //     }
            // }
            // Loading()
            println("Loading...")
        // } else {
        //     RunTaskButton(
        //         onClick = { shouldRunTask = true },
        //         text = "Click to run task: $task",
        //         modifier = Modifier
        //             .align(Alignment.CenterHorizontally)
        //             .padding(Constants.DEFAULT_PADDING)
        //     )
        //
        //     if (output.isNotEmpty()) {
        //         Box(modifier) {
        //             Column(
        //                 modifier = Modifier
        //                     .verticalScroll(scrollState)
        //                     .padding(Constants.DEFAULT_PADDING)
        //             ) {
        //                 Text(
        //                     text = "Task '$task' output:",
        //                     fontSize = Constants.HEADING_FONT_SIZE,
        //                     lineHeight = Constants.DEFAULT_LINE_HEIGHT,
        //                     textAlign = TextAlign.Left,
        //                 )
        //                 Text(
        //                     text = output,
        //                     fontSize = Constants.DEFAULT_FONT_SIZE,
        //                     lineHeight = Constants.DEFAULT_LINE_HEIGHT,
        //                     textAlign = TextAlign.Left,
        //                 )
        //             }
        //
        //             VerticalScrollbar(
        //                 modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
        //                 adapter = rememberScrollbarAdapter(scrollState)
        //             )
        //         }
        //     }
        // }
    // }
}
