package moe.nerinyan.apiv3.service.osz

import moe.nerinyan.apiv3.dto.OszOptionDTO
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.io.FileNotFoundException

@Service
class OszFileServiceImpl : OszFileService {

    override fun exists(option: OszOptionDTO): Mono<Boolean> {
        return Mono.fromCallable { FileSystemResource(filePath(option)).exists() }
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