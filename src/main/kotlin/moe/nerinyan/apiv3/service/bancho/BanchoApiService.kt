package moe.nerinyan.apiv3.service.bancho

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Service
class BanchoApiService(
    private val banchoAuthService: BanchoAuthService,
    private val webClient: WebClient,
) {
    private val log = KotlinLogging.logger {}

    // 내부 처리용
    // 비트맵 1개 조회
    fun getBeatmap(mapId: Long): Mono<String> {
        return banchoAuthService.getAccessTokenHeader()
            .flatMap { header ->
                webClient.get()
                    .uri("https://osu.ppy.sh/api/v2/beatmapsets/search?nsfw=true&s=any&q=$mapId")
                    .headers(header)
                    .retrieve()
                    .bodyToMono(String::class.java)
            }
    }

    fun getBeatmapSet(setId: Long): Nothing = TODO() //비트맵 셋 조회

    fun getBeatmapPacks(): Nothing = TODO() //비트맵 팩 리스트 조회
    fun getBeatmapPack(pack: String): Nothing = TODO() //비트맵 팩 조회

    fun getBeatmapSetListByAllAsc(): Nothing = TODO() // 모든 맵 asc
    fun getBeatmapSetListByAllDesc(): Nothing = TODO() // 모든 맵 desc
    fun getBeatmapSetListByRankedDesc(): Nothing = TODO() // 랭크드 맵 desc
    fun getBeatmapSetListByQualifiedDesc(): Nothing = TODO() // 퀄리파이드 맵 desc
    fun getBeatmapSetListByGraveyardDesc(): Nothing = TODO() // 그레이브야드 맵 desc
    fun getBeatmapSetListByLovedDesc(): Nothing = TODO() // 러브드 맵 desc


}