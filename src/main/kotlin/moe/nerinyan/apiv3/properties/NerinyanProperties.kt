package moe.nerinyan.apiv3.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "nerinyan")
class NerinyanProperties(
    var dirPolicy: DirPolicy = DirPolicy(),
) {
    class DirPolicy {
        var maxSize: String = ""
        var oszDir: String = ""
    }
}

