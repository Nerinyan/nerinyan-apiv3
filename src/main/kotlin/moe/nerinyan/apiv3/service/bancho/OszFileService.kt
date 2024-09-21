package moe.nerinyan.apiv3.service.bancho

import moe.nerinyan.apiv3.dto.OszOptionDTO
import org.springframework.core.io.Resource
import org.springframework.core.io.buffer.DataBuffer
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono


interface OszFileService {
    fun filePath(option: OszOptionDTO): String {
        return buildString {
            append("./osz/${option.id}/${option.id}")
            if (option.noBg) append("_NB")
            if (option.noHitSound) append("_NH")
            if (option.noStoryboard) append("_NS")
            if (option.noVideo) append("_NV")
            append(".osz")
        }
    }

    fun exists(option: OszOptionDTO): Mono<Boolean>
    fun saveOsz(option: OszOptionDTO, dataBufferFlux: Flux<DataBuffer>)
    fun getFile(option: OszOptionDTO): Mono<Resource>

}