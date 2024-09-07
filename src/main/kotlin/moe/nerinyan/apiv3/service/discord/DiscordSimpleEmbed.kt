package moe.nerinyan.apiv3.service.discord

import com.fasterxml.jackson.annotation.JsonInclude
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class DiscordSimpleEmbed {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    var embeds: MutableList<Embeds> = mutableListOf()

    var username: String? = null

    class Embeds {
        @JsonInclude(JsonInclude.Include.NON_NULL)
        var title: String? = ""

        @JsonInclude(JsonInclude.Include.NON_NULL)
        var description: String? = ""

        @JsonInclude(JsonInclude.Include.NON_NULL)
        var timestamp: String? = ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX"))

    }

}