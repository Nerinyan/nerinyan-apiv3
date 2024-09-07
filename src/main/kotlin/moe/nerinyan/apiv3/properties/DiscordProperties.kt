package moe.nerinyan.apiv3.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource

@Configuration
@PropertySource(value = [
    "./application-discord.yml", // use this for server
    "classpath:application-discord.yml"
])
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

