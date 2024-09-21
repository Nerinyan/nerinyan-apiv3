package moe.nerinyan.apiv3.service.bancho

import io.github.oshai.kotlinlogging.KotlinLogging
import moe.nerinyan.apiv3.dto.OszOptionDTO
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class BanchoApiService(
    private val banchoAuthService: BanchoAuthService,
    private val webClient: WebClient,
    private val oszFileService: OszFileService,
) {
    private val log = KotlinLogging.logger {}

    // openapi
    fun downloadOsz(option: OszOptionDTO): Flux<DataBuffer> {
        return banchoAuthService.getAccessTokenHeader()
            .flatMapMany { headers ->
                log.info { "Downloading osz file from bancho" }
                webClient.get()
                    .uri("https://osu.ppy.sh/api/v2/beatmapsets/${option.id}/download")
                    .headers(headers)
                    .retrieve()
                    .onStatus({ it.is4xxClientError }) { response -> is4xxServerError(response) }
                    .onStatus({ it.is5xxServerError }) { response -> is5xxServerError(response) }
                    .bodyToFlux(DataBuffer::class.java)
            }
            .publish()
            .autoConnect(2)
            .also { flux -> oszFileService.saveOsz(option, flux) }
    }

    // 내부 처리용
    fun getBeatmap(mapId: Long): Nothing = TODO() // 비트맵 1개 조회
    fun getBeatmapSet(setId: Long): Nothing = TODO() //비트맵 셋 조회

    fun getBeatmapSetListByAllAsc(): Nothing = TODO() // 모든 맵 asc
    fun getBeatmapSetListByAllDesc(): Nothing = TODO() // 모든 맵 desc
    fun getBeatmapSetListByRankedDesc(): Nothing = TODO() // 랭크드 맵 desc
    fun getBeatmapSetListByQualifiedDesc(): Nothing = TODO() // 퀄리파이드 맵 desc
    fun getBeatmapSetListByGraveyardDesc(): Nothing = TODO() // 그레이브야드 맵 desc
    fun getBeatmapSetListByLovedDesc(): Nothing = TODO() // 러브드 맵 desc

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