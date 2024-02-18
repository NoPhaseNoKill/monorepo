import com.nophasenokill.PerformanceMetricConsolePublisher
import com.nophasenokill.PerformanceMetricJsonPublisher

plugins {
    id("io.github.cdsap.talaiot")
}

talaiot {

    publishers {
        customPublishers(
            PerformanceMetricConsolePublisher(),
            PerformanceMetricJsonPublisher(project.layout.projectDirectory.asFile.path)
        )
    }
    metrics {
        generateBuildId = true
    }
}

