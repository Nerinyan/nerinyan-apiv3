package moe.nerinyan.apiv3.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import moe.nerinyan.apiv3.dto.OszOptionDTO
import moe.nerinyan.apiv3.service.bancho.BanchoOszService
import org.springdoc.core.annotations.ParameterObject
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono


@Tag(name = "osz", description = "osz download api")
@RestController
class OszDownloadController(
    private val banchoOszService: BanchoOszService
) {

    @Operation(description = "osz cached in server?")
    @GetMapping("/api/v3/osz/cached/{id}")
    fun oszCached(@ParameterObject params: OszOptionDTO): Mono<Boolean> {
        return banchoOszService.exists(params)
    }

    @Operation(description = "osz download")
    @GetMapping(path = ["/api/v3/osz/download/{id}"])
    fun oszDownloadTest(@ParameterObject params: OszOptionDTO): ResponseEntity<Flux<DataBuffer>> {
        val fileFlux: Flux<DataBuffer> = banchoOszService.downloadOsz(params)

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"beatmapset_${params.id}.osz\"")
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(fileFlux)  // Flux<DataBuffer>를 클라이언트에 스트리밍
    }

    @GetMapping("/api/v3/osz/refresh")
    fun oszRefresh() {

    }


}