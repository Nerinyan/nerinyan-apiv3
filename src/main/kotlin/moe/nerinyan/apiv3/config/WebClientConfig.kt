package moe.nerinyan.apiv3.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient

@Configuration
class WebClientConfig {
    @Bean
    fun webClient(): WebClient {
        val httpClient = HttpClient.create()
            .followRedirect(true)  // 리디렉션 자동 추적 설정
        
        return WebClient.builder()
            .clientConnector(ReactorClientHttpConnector(httpClient))
            .build()
    }
}