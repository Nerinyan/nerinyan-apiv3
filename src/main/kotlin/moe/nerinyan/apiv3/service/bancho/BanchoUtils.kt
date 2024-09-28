package moe.nerinyan.apiv3.service.bancho

import org.springframework.web.reactive.function.client.ClientResponse
import reactor.core.publisher.Mono

object BanchoUtils {
    fun is4xxServerError(response: ClientResponse): Mono<Nothing> {
        return response.bodyToMono(String::class.java).flatMap { body ->
            val errorMono: Mono<Nothing> = when (response.statusCode().value()) {
                400 -> Mono.error(Bancho400Exception(body))
                401 -> Mono.error(Bancho401Exception(body))
                403 -> Mono.error(Bancho403Exception(body))
                404 -> Mono.error(Bancho404Exception(body))
                429 -> Mono.error(Bancho429Exception(body))
                else -> Mono.error(Bancho4xxException(body))
            }
            errorMono
        }
    }

    fun is5xxServerError(response: ClientResponse): Mono<Nothing> {
        return response.bodyToMono(String::class.java).flatMap { body ->
            Mono.error(Bancho5xxException(body))
        }
    }
}

