package moe.nerinyan.apiv3.service.discord

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.oshai.kotlinlogging.KotlinLogging
import moe.nerinyan.apiv3.properties.DiscordProperties
import moe.nerinyan.apiv3.service.NotificationService
import org.springframework.http.RequestEntity.post
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono


@Service
class DiscordWebhookService(
    private val webClient: WebClient,
    private val discordProperties: DiscordProperties,
    private val objectMapper: ObjectMapper,
) : NotificationService {

    private val log = KotlinLogging.logger{}

    fun sendEmbed(url: String, title: String) = sendEmbed(url, title, null)

    fun sendEmbed(url: String, title: String, description: String?) {
        if (url.isEmpty()) {
            log.debug { "URL is empty ignore this request" }
            return
        }
        webClient.post()
            .uri(url)
            .header("Content-Type", "application/json")
            .bodyValue(DiscordSimpleEmbed().apply {
                username = "Nerinyan API Server"
                embeds.add(DiscordSimpleEmbed.Embeds().apply {
                    this.title = title
                    this.description = description
                })
            })
            .retrieve()
            .bodyToMono(Void::class.java)
            .doOnError { error ->
                log.error { "Failed to send Discord webhook: ${error.message}" }
            }
            .block()
    }

    override fun startup() {
        sendEmbed(discordProperties.webhook.startup, "Nerinyan API Server has been started")
    }

    override fun shutdown() {
        sendEmbed(discordProperties.webhook.startup, "Nerinyan API Server has been stopped")
    }

    override fun info(message: String) {
        sendEmbed(discordProperties.webhook.info, "INFO", message)
    }

    override fun error(message: String) {
        sendEmbed(discordProperties.webhook.error, "ERROR", message)
    }

    override fun error(exception: Exception) {
        sendEmbed(discordProperties.webhook.error, "ERROR | ${exception.javaClass.simpleName}", exception.message)
    }

}