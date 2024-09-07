package moe.nerinyan.apiv3.controller

import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import java.net.URI


@RestController
class CommonController {

    @Operation(hidden = true)
    @GetMapping(path = ["/"])
    fun redirectToSwagger(response: ServerHttpResponse): Mono<Void> {
        response.statusCode = HttpStatus.PERMANENT_REDIRECT
        response.headers.location = URI.create("/webjars/swagger-ui/index.html")
        return response.setComplete()
    }

    @GetMapping("/healthy")
    fun healthy(): Mono<String> {
        return Mono.just("ok")
    }


}