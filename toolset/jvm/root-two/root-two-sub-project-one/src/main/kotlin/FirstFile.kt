import java.io.File

class FirstFile {

    fun print(): String {
        return File(System.getProperty("user.dir")).toString()
    }
}