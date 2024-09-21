package moe.nerinyan.apiv3.service.bancho

import org.springframework.stereotype.Service

@Service
class BanchoApiService(
    private val banchoAuthService: BanchoAuthService,
) {
    // openapi
    fun downloadOsz(setId: Long): Nothing = TODO() // 비트맵셋 다운로드

    // 내부 처리용
    fun getBeatmap(mapId: Long): Nothing = TODO() // 비트맵 1개 조회
    fun getBeatmapSet(setId: Long): Nothing = TODO() //비트맵 셋 조회

    fun getBeatmapSetListByAllAsc(): Nothing = TODO() // 모든 맵 asc
    fun getBeatmapSetListByAllDesc(): Nothing = TODO() // 모든 맵 desc
    fun getBeatmapSetListByRankedDesc(): Nothing = TODO() // 랭크드 맵 desc
    fun getBeatmapSetListByQualifiedDesc(): Nothing = TODO() // 퀄리파이드 맵 desc
    fun getBeatmapSetListByGraveyardDesc(): Nothing = TODO() // 그레이브야드 맵 desc
    fun getBeatmapSetListByLovedDesc(): Nothing = TODO() // 러브드 맵 desc

}