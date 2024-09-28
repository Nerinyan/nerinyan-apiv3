package moe.nerinyan.apiv3.service.bancho

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class BanchoApiService(
    private val banchoAuthService: BanchoAuthService,
    private val webClient: WebClient,
) {
    private val log = KotlinLogging.logger {}

    // 내부 처리용
    // 비트맵 1개 조회
    fun getBeatmapSet(setId: Long): Mono<BeatmapSet> {
        return banchoAuthService.getAccessTokenHeader()
            .flatMap { header ->
                webClient.get()
                    .uri("https://osu.ppy.sh/api/v2/beatmapsets/$setId")
                    .headers(header)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .retrieve()
                    .bodyToMono(BeatmapSet::class.java)
            }
    }

    fun getBeatmap(mapId: Long): Nothing = TODO() //비트맵 셋 조회

    fun getBeatmapPacks(): Nothing = TODO() //비트맵 팩 리스트 조회
    fun getBeatmapPack(pack: String): Nothing = TODO() //비트맵 팩 조회

    fun getBeatmapSetListByAllAsc(): Flux<BeatmapSet> = TODO() // 모든 맵 asc
    fun getBeatmapSetListByAllDesc(): Flux<BeatmapSet> = TODO() // 모든 맵 desc
    fun getBeatmapSetListByRankedDesc(): Flux<BeatmapSet> = TODO() // 랭크드 맵 desc
    fun getBeatmapSetListByQualifiedDesc(): Flux<BeatmapSet> = TODO() // 퀄리파이드 맵 desc
    fun getBeatmapSetListByGraveyardDesc(): Flux<BeatmapSet> = TODO() // 그레이브야드 맵 desc
    fun getBeatmapSetListByLovedDesc(): Flux<BeatmapSet> = TODO() // 러브드 맵 desc


}