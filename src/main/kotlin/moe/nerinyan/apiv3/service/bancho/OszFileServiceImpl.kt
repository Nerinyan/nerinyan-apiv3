package moe.nerinyan.apiv3.service.bancho

import io.github.oshai.kotlinlogging.KotlinLogging
import moe.nerinyan.apiv3.dto.OszOptionDTO
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource
import org.springframework.core.io.buffer.DataBuffer
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

        dataBufferFlux
            .subscribeOn(Schedulers.boundedElastic())
            .doOnNext { dataBuffer ->
                // 파일에 데이터를 씀
                val readableByteCount = dataBuffer.readableByteCount()
                val byteArray = ByteArray(readableByteCount)
                dataBuffer.read(byteArray)
                Files.write(path, byteArray, StandardOpenOption.CREATE, StandardOpenOption.APPEND)
            }
            .doOnComplete {
                log.info { "File write complete." }
            }
            .doOnError { error ->
                log.error { "Error writing to file: ${error.message}" }
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