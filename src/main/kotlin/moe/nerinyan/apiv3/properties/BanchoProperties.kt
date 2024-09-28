package moe.nerinyan.apiv3.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "bancho")
class BanchoProperties(
    var auth: Auth = Auth()
) {
    class Auth {
        var clientId: String = ""
        var clientSecret: String = ""
        var scope: String = ""
        var url: String = ""
        var username: String = ""
        var password: String = ""
    }

    class Crawler {
        var baseUrl: String = ""
    }
}

