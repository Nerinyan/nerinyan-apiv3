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
        val a = token.validate()
        log.info { a }
        return when (a) {
            BanchoTokenRenewalType.VALID -> Mono.just(Consumer { headers -> setAuthHeader(headers) })

            BanchoTokenRenewalType.REFRESH -> refresh().flatMap {
                log.info { "refresh success" }
                token = it
                Mono.just(Consumer<HttpHeaders> { headers -> setAuthHeader(headers) })
            }.onErrorResume {
                log.error { "Failed to refresh. Trying to login." }
                login().flatMap {
                    log.info { "Login success" }
                    token = it
                    Mono.just(Consumer<HttpHeaders> { headers -> setAuthHeader(headers) })
                }.doOnError { e ->
                    log.error { e }
                    throw e
                }
            }

            BanchoTokenRenewalType.LOGIN -> login().flatMap {
                log.info { "Login success" }
                token = it
                Mono.just(Consumer<HttpHeaders> { headers -> setAuthHeader(headers) })
            }.doOnError { e ->
                log.error { e }
                throw e
            }
        }

    }

    private fun setAuthHeader(headers: HttpHeaders) {
        headers.set(HttpHeaders.AUTHORIZATION, "${token.tokenType} ${token.accessToken}")
    }

    private fun login(): Mono<BanchoTokenDTO> {
        return webClient.post().uri(banchoProperties.auth.url)
            .header(CONTENT_TYPE, MULTIPART_FORM_DATA_VALUE)
            .bodyValue(
                MultipartBodyBuilder().apply {
                    part("client_id", banchoProperties.auth.clientId)
                    part("client_secret", banchoProperties.auth.clientSecret)
                    part("scope", banchoProperties.auth.scope)
                    part("grant_type", "password")
                    part("username", banchoProperties.auth.username)
                    part("password", banchoProperties.auth.password)
                }.build()
            )
            .retrieve()
            .onStatus({ it.is4xxClientError }) { r -> is4xxServerError(r) }
            .onStatus({ it.is5xxServerError }) { r -> is5xxServerError(r) }
            .bodyToMono(BanchoTokenDTO::class.java)
    }

    private fun refresh(): Mono<BanchoTokenDTO> {
        return webClient.post().uri(banchoProperties.auth.url)
            .header(CONTENT_TYPE, MULTIPART_FORM_DATA_VALUE)
            // refresh 는 auth 토큰이 필요함
            .header("Authorization", "${token.tokenType} ${token.accessToken}")
            .bodyValue(
                MultipartBodyBuilder().apply {
                    part("client_id", banchoProperties.auth.clientId)
                    part("client_secret", banchoProperties.auth.clientSecret)
                    part("scope", banchoProperties.auth.scope)
                    part("grant_type", "refresh_token")
                    part("refresh_token", token.refreshToken)
                }.build()
            )
            .retrieve()
            .onStatus({ it.is4xxClientError }) { r -> is4xxServerError(r) }
            .onStatus({ it.is5xxServerError }) { r -> is5xxServerError(r) }
            .bodyToMono(BanchoTokenDTO::class.java)
    }

    private fun is4xxServerError(response: ClientResponse): Mono<Throwable> {
        return response.bodyToMono(String::class.java).flatMap { body ->
            val exception: Throwable = when (response.statusCode().value()) {
                400 -> Bancho400Exception(body)
                401 -> Bancho401Exception(body)
                403 -> Bancho403Exception(body)
                404 -> Bancho404Exception(body)
                429 -> Bancho429Exception(body)
                else -> Bancho4xxException(body)
            }
            log.error { "4xx Error: ${response.statusCode()} - $body" }
            Mono.error(exception)
        }
    }

    private fun is5xxServerError(response: ClientResponse): Mono<Throwable> {
        return response.bodyToMono(String::class.java).flatMap { body ->
            val exception = Bancho5xxException(body)
            log.error { "5xx Error: ${response.statusCode()} - $body" }
            Mono.error(exception)
        }
    }
}

