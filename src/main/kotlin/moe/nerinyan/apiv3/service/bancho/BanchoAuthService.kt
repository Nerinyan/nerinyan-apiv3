package moe.nerinyan.apiv3.service.bancho

import io.github.oshai.kotlinlogging.KotlinLogging
import moe.nerinyan.apiv3.properties.BanchoProperties
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpHeaders.CONTENT_TYPE
import org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE
import org.springframework.http.client.MultipartBodyBuilder
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import java.util.function.Consumer


@Service
class BanchoAuthService(
    private var banchoProperties: BanchoProperties,
    private val webClient: WebClient,
) {
    private val log = KotlinLogging.logger {}
    private var token: BanchoTokenDTO = BanchoTokenDTO()

    fun getAccessTokenHeader(): Mono<Consumer<HttpHeaders>> {
        return when (token.validate()) {
            BanchoTokenRenewalType.VALID -> Mono.just(Consumer { headers ->
                headers.set("Authorization", "${token.tokenType} ${token.accessToken}")
            })

            BanchoTokenRenewalType.REFRESH -> refresh().flatMap {
                token = it
                Mono.just(Consumer<HttpHeaders> { headers ->
                    headers.set("Authorization", "${token.tokenType} ${token.accessToken}")
                })
            }.onErrorResume {
                log.error { "Failed to refresh." }
                log.info { "Trying to login." }
                login().flatMap {
                    token = it
                    Mono.just(Consumer<HttpHeaders> { headers ->
                        headers.set("Authorization", "${token.tokenType} ${token.accessToken}")
                    })
                }.doOnError { e -> throw e }
            }

            BanchoTokenRenewalType.LOGIN -> login().flatMap {
                token = it
                Mono.just(Consumer<HttpHeaders> { headers ->
                    headers.set("Authorization", "${token.tokenType} ${token.accessToken}")
                })
            }.doOnError { e -> throw e }
        }

    }


    private fun login(): Mono<BanchoTokenDTO> {
        return webClient.post().uri(banchoProperties.url)
            .header(CONTENT_TYPE, MULTIPART_FORM_DATA_VALUE)
            .bodyValue(
                MultipartBodyBuilder().apply {
                    part("client_id", banchoProperties.clientId)
                    part("client_secret", banchoProperties.clientSecret)
                    part("scope", banchoProperties.scope)
                    part("grant_type", "password")
                    part("username", banchoProperties.username)
                    part("password", banchoProperties.password)
                }.build()
            )
            .retrieve()
            .onStatus({ it.is4xxClientError }) { response -> is4xxServerError(response) }
            .onStatus({ it.is5xxServerError }) { response -> is5xxServerError(response) }
            .bodyToMono(BanchoTokenDTO::class.java)

    }

    private fun refresh(): Mono<BanchoTokenDTO> {
        return webClient.post().uri(banchoProperties.url)
            .header(CONTENT_TYPE, MULTIPART_FORM_DATA_VALUE)
            // refresh 는 auth 토큰이 필요함
            .header("Authorization", "${token.tokenType} ${token.accessToken}")
            .bodyValue(
                MultipartBodyBuilder().apply {
                    part("client_id", banchoProperties.clientId)
                    part("client_secret", banchoProperties.clientSecret)
                    part("scope", banchoProperties.scope)
                    part("grant_type", "refresh_token")
                    part("refresh_token", token.refreshToken)
                }.build()
            )
            .retrieve()
            .onStatus({ it.is4xxClientError }) { response -> is4xxServerError(response) }
            .onStatus({ it.is5xxServerError }) { response -> is5xxServerError(response) }
            .bodyToMono(BanchoTokenDTO::class.java)
    }

    private fun is4xxServerError(response: ClientResponse): Mono<Nothing> {
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

    private fun is5xxServerError(response: ClientResponse): Mono<Nothing> {
        return response.bodyToMono(String::class.java).flatMap { body ->
            Mono.error(Bancho5xxException(body))
        }
    }
}

