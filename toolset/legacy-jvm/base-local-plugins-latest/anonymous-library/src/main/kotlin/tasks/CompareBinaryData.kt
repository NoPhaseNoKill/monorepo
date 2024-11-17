package tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.rocksdb.Options
import org.rocksdb.RocksDB
import org.rocksdb.WALRecoveryMode
import java.io.File
import java.security.MessageDigest

abstract class CompareBinaryDataTask : DefaultTask() {

    init {
        description = "Compares the current binary file data to previously cached data in RocksDB."
    }

    @TaskAction
    fun compareData() {
        val binaryFile = File("${project.layout.buildDirectory.get()}/resources/main/kotlin-stdlib-1_9_24_jar-snapshot.bin")
        val chunkSize = 1024
        val dbPath = File("${project.layout.buildDirectory.get()}/custom-tasks/store-binary-data-in-cache/rocksdb-cache")
        dbPath.mkdirs()

        println("Comparing binary data with cache at path: ${dbPath.absolutePath}")

        RocksDB.loadLibrary()
        val options = Options().setCreateIfMissing(true).apply {
            setMaxBackgroundJobs(4)
            setMaxOpenFiles(5000)
            setCompactionReadaheadSize(4 * 1024 * 1024)
            setMaxTotalWalSize(256 * 1024 * 1024)
            setBytesPerSync(256 * 1024 * 1024)
            setDelayedWriteRate(32 * 1024 * 1024)
            setUseAdaptiveMutex(true)
            setWalRecoveryMode(WALRecoveryMode.PointInTimeRecovery)
        }

        options.use { opt ->
            RocksDB.open(opt, dbPath.absolutePath).use { db ->
                binaryFile.inputStream().use { inputStream ->
                    val buffer = ByteArray(chunkSize)
                    var bytesRead: Int
                    var chunkIndex = 0
                    var hasDifferences = false

                    while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                        val md = MessageDigest.getInstance("SHA-256")
                        val currentChunkHash = md.digest(buffer.copyOfRange(0, bytesRead))

                        val storedChunkHash = db.get("chunk_$chunkIndex".toByteArray())

                        if (storedChunkHash == null) {
                            println("No stored hash found for chunk $chunkIndex (new data).")
                            hasDifferences = true
                        } else if (!currentChunkHash.contentEquals(storedChunkHash)) {
                            println("Difference found in chunk $chunkIndex.")
                            hasDifferences = true
                        }

                        chunkIndex++
                    }

                    if (!hasDifferences) {
                        println("No differences found between the binary file and cached data.")
                    } else {
                        println("Differences detected. Comparison complete.")
                    }
                }
            }
        }
    }
}
