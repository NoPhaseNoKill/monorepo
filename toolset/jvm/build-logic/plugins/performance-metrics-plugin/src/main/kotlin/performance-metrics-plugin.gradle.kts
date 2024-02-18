import com.nophasenokill.CustomPublisher
import com.nophasenokill.MyJsonPublisher

plugins {
    id("io.github.cdsap.talaiot")
}

talaiot {

    publishers {
        customPublishers(
            CustomPublisher(),
            MyJsonPublisher(project.layout.buildDirectory.get().asFile.path)
        )
    }
    metrics {
        generateBuildId = true
    }
}

