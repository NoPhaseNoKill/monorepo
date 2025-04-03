import com.nophasenokill.extensions.findCatalog
import com.nophasenokill.extensions.findCatalogVersion

val versionCatalog = project.findCatalog()

group = "com.nophasenokill.$name"
version = versionCatalog.findCatalogVersion("groupVersion")
