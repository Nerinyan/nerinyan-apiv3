package moe.nerinyan.apiv3.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource

@Configuration
@PropertySource(
    value = [
        "./application-bancho.yml", // use this for server
        "classpath:application-bancho.yml"
    ]
)
@ConfigurationProperties(prefix = "bancho")
class BanchoProperties {
    var clientId: String = ""
    var clientSecret: String = ""
    var scope: String = ""
    var url: String = ""
    var username: String = ""
    var password: String = ""
}

