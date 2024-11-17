package tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.rocksdb.Options
import org.rocksdb.RocksDB
import org.rocksdb.RocksIterator
import org.rocksdb.WALRecoveryMode
import java.io.File

abstract class InspectRocksDBTask : DefaultTask() {

    init {
        description = "Inspects and prints the contents of the RocksDB database."
    }

    @TaskAction
    fun inspectData() {
        val dbPath = File("${project.layout.buildDirectory.get()}/custom-tasks/store-binary-data-in-cache/rocksdb-cache")

        if (!dbPath.exists()) {
            println("The RocksDB directory does not exist at path: ${dbPath.absolutePath}")
            return
        }

        RocksDB.loadLibrary()
        val options = Options().setCreateIfMissing(false).apply {
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
                val iterator: RocksIterator = db.newIterator()
                iterator.seekToFirst()

                println("Inspecting contents of RocksDB at ${dbPath.absolutePath}:")
                while (iterator.isValid) {
                    val key = String(iterator.key())
                    val value = iterator.value().joinToString("") { "%02x".format(it) }
                    println("Key: $key, Value (hex): $value")
                    iterator.next()
                }
            }
        }
    }
}
