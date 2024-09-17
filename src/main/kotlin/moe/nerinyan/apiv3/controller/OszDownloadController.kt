package moe.nerinyan.apiv3.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import moe.nerinyan.apiv3.dto.OszOptionDTO
import moe.nerinyan.apiv3.service.osz.OszFileService
import org.springdoc.core.annotations.ParameterObject
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ZeroCopyHttpOutputMessage
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import java.io.File


@Tag(name = "osz", description = "osz download api")
@RestController
class OszDownloadController(
    private val oszFileService: OszFileService
) {

    @Operation(description = "osz cached in server?")
    @GetMapping("/api/v3/osz/cached/{id}")
    fun oszCached(@ParameterObject params: OszOptionDTO): Mono<Boolean> {
        return oszFileService.exists(params)
    }

    @Operation(description = "osz download")
    @GetMapping(path = ["/api/v3/osz/download/{id}"])
    fun oszDownload(@ParameterObject params: OszOptionDTO, response: ServerHttpResponse): Mono<Void> {
        return oszFileService.getFile(params).flatMap { resource ->
            val file: File = resource.file

            // 응답 헤더 설정
            response.headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"${params.id}.osz\"")
            response.headers.contentType = MediaType.APPLICATION_OCTET_STREAM
            response.headers.contentLength = file.length()

            return@flatMap (response as ZeroCopyHttpOutputMessage).writeWith(file, 0, file.length())
        }
    }

    @GetMapping("/api/v3/osz/refresh")
    fun oszRefresh() {

    }


}