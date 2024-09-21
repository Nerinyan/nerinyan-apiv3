package moe.nerinyan.apiv3.service.bancho

import io.github.oshai.kotlinlogging.KotlinLogging
import moe.nerinyan.apiv3.dto.OszOptionDTO
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.core.io.buffer.DataBufferUtils
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.io.FileNotFoundException
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

@Service
class OszFileServiceImpl : OszFileService {
    private val log = KotlinLogging.logger {}

    override fun exists(option: OszOptionDTO): Mono<Boolean> {
        return Mono.fromCallable { FileSystemResource(filePath(option)).exists() }
    }

    override fun saveOsz(option: OszOptionDTO, dataBufferFlux: Flux<DataBuffer>) {
        val path = Paths.get(filePath(option))
        if (!Files.exists(path.parent)) Files.createDirectories(path.parent)

        // 파일 쓰기 작업
        DataBufferUtils.write(dataBufferFlux.map { buffer -> DataBufferUtils.retain(buffer) }, path, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)
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
            }
            .subscribe()
    }

    override fun getFile(option: OszOptionDTO): Mono<Resource> {
        val resource = FileSystemResource(filePath(option))
        return if (resource.exists()) {
            Mono.just(resource)
        } else {
            Mono.error(FileNotFoundException("File not found: ${filePath(option)}"))
        }
    }
}