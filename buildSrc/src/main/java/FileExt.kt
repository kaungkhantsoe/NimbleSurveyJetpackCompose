import java.io.File
import java.util.*

fun File.loadProperties(fileName: String): Properties {
    val properties = Properties()
    val propertiesFile = File(this, fileName)

    if (propertiesFile.isFile) {
        properties.load(propertiesFile.inputStream())
    }
    return properties
}
