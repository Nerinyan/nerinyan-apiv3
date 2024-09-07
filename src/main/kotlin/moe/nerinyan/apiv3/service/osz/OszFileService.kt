package moe.nerinyan.apiv3.service.osz

import moe.nerinyan.apiv3.dto.OszOptionDTO
import org.springframework.core.io.Resource
import reactor.core.publisher.Mono


interface OszFileService {
    fun filePath(option: OszOptionDTO): String {
        return buildString {
            append("./beatmaps/${option.id}/${option.id}")
            if (option.noBg) append("_NB")
            if (option.noHitSound) append("_NH")
            if (option.noStoryboard) append("_NS")
            if (option.noVideo) append("_NV")
            append(".osz")
        }
    }
    fun exists( option: OszOptionDTO): Mono<Boolean>
    fun getFile( option: OszOptionDTO): Mono<Resource>

}