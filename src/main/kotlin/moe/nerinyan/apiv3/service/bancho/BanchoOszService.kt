package moe.nerinyan.apiv3.service.bancho

import io.github.oshai.kotlinlogging.KotlinLogging
import moe.nerinyan.apiv3.dto.OszOptionDTO
import moe.nerinyan.apiv3.properties.NerinyanProperties
import moe.nerinyan.apiv3.service.bancho.BanchoUtils.is4xxServerError
import moe.nerinyan.apiv3.service.bancho.BanchoUtils.is5xxServerError
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.core.io.buffer.DataBufferUtils
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

@Service
class BanchoOszService(
    private val banchoAuthService: BanchoAuthService,
    private val webClient: WebClient,
    private val nerinyanProperties: NerinyanProperties
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
            .also { flux -> saveOsz(option, flux) }
    }

    fun filePath(option: OszOptionDTO): String {
        return buildString {
            append("${nerinyanProperties.dirPolicy.oszDir}/${option.id}/${option.id}")
            if (option.noBg) append("_NB")
            if (option.noHitSound) append("_NH")
            if (option.noStoryboard) append("_NS")
            if (option.noVideo) append("_NV")
            append(".osz")
        }
    }

    fun exists(option: OszOptionDTO): Mono<Boolean> {
        return Mono.fromCallable { FileSystemResource(filePath(option)).exists() }
    }

    fun saveOsz(option: OszOptionDTO, dataBufferFlux: Flux<DataBuffer>) {
        val tmpPath = Paths.get("${filePath(option)}.${System.currentTimeMillis()}") // 임시 경로
        val path = Paths.get(filePath(option)) // 최종 경로

        if (!Files.exists(path.parent)) Files.createDirectories(path.parent)

        // 파일 쓰기 작업
        DataBufferUtils.write(
            dataBufferFlux.map { buffer -> DataBufferUtils.retain(buffer) },
            tmpPath, // 임시 경로에 저장
            StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING
        )
            .subscribeOn(Schedulers.boundedElastic())  // I/O 스레드에서 실행
            .then()  // 파일 저장이 완료된 후 처리
            .doOnTerminate {
                log.info { "File write complete." }
            }
            .doOnError { error ->
                log.error { "Error writing to file: ${error.message}" }
            }
            .doFinally {
                dataBufferFlux.subscribe { buffer -> DataBufferUtils.release(buffer) }
                Files.move(tmpPath, path) // 최종 경로로  변경
            }
            .subscribe()
    }

}