package moe.nerinyan.apiv3.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "discord")
class DiscordProperties(
    var webhook: Webhook = Webhook(),
) {

    class Webhook(
        var startup: String = "",
        var shutdown: String = "",
        var info: String = "",
        var error: String = "",
    )


}

