package moe.nerinyan.apiv3.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import moe.nerinyan.apiv3.dto.OszOptionDTO
import moe.nerinyan.apiv3.service.bancho.BanchoApiService
import moe.nerinyan.apiv3.service.bancho.OszFileService
import org.springdoc.core.annotations.ParameterObject
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.ZeroCopyHttpOutputMessage
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.io.File


@Tag(name = "osz", description = "osz download api")
@RestController
class OszDownloadController(
    private val oszFileService: OszFileService,
    private val banchoApiService: BanchoApiService,
) {

    @Operation(description = "osz cached in server?")
    @GetMapping("/api/v3/osz/cached/{id}")
    fun oszCached(@ParameterObject params: OszOptionDTO): Mono<Boolean> {
        return oszFileService.exists(params)
    }

    @Operation(description = "osz download")
    @GetMapping(path = ["/api/v3/osz/download/{id}/test"])
    fun oszDownloadTest(@ParameterObject params: OszOptionDTO, response: ServerHttpResponse): ResponseEntity<Flux<DataBuffer>> {
        val fileFlux: Flux<DataBuffer> = banchoApiService.downloadOsz(params)

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"beatmapset_${params.id}.osz\"")
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(fileFlux)  // Flux<DataBuffer>를 클라이언트에 스트리밍
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