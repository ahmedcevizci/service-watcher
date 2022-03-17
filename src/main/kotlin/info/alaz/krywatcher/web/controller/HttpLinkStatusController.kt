package info.alaz.krywatcher.web.controller

import info.alaz.krywatcher.db.entity.HttpLinkStatus
import info.alaz.krywatcher.service.HttpLinkStatusService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.codec.ServerSentEvent
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import java.time.Duration

@RestController
@RequestMapping("/http-links-status")
class HttpLinkStatusController(private val httpLinkStatusService: HttpLinkStatusService) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @GetMapping(produces = ["application/json"])
    fun readAll(): ResponseEntity<List<HttpLinkStatus>> {
        return ResponseEntity(this.httpLinkStatusService.findAll(), HttpStatus.OK)
    }

    @GetMapping(value = ["/auto-refresh"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun readAllWithAutoRefresh(): Flux<ServerSentEvent<List<HttpLinkStatus>>> {
        return Flux.interval(Duration.ofSeconds(1))
            .map { httpLinkStatusService.findAll() }
            .map <ServerSentEvent<List<HttpLinkStatus>>>
            { httpLinkStatusList ->
                ServerSentEvent.builder<List<HttpLinkStatus>>()
                    .event("http-links-status-changed")
                    .data(httpLinkStatusList)
                    .build()
            }
    }
}
