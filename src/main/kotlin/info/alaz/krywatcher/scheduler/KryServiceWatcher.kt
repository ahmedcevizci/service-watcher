package info.alaz.krywatcher.scheduler

import info.alaz.krywatcher.db.entity.HttpLink
import info.alaz.krywatcher.db.entity.SupportedHttpMethod
import info.alaz.krywatcher.service.HttpLinkService
import info.alaz.krywatcher.service.HttpLinkStatusService
import org.slf4j.LoggerFactory
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.http.codec.ClientCodecConfigurer
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration
import javax.annotation.PostConstruct


@Component
class KryServiceWatcher(
    private val kryServiceWatcherConfig: KryServiceWatcherConfig,
    private val httpLinkService: HttpLinkService,
    private val httpLinkStatusService: HttpLinkStatusService,
) : PeriodicReactiveTask<HttpLink>(
    HttpLink::class.java,
    "KryServiceWatcher",
    kryServiceWatcherConfig.initialWaitInMs,
    kryServiceWatcherConfig.runIntervalInMs,
    Long.MAX_VALUE,
    8
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    val strategies = ExchangeStrategies.builder()
        .codecs { codecs: ClientCodecConfigurer ->
            codecs.defaultCodecs().maxInMemorySize(16 * 1024 * 1024)
        }
        .build()

    private val webClient: WebClient = WebClient.builder().clientConnector(ReactorClientHttpConnector())
        .exchangeStrategies(strategies)
        .build()

    override fun processInitialTasks(cycleNumber: Long): Mono<*> {
        return Mono.just(cycleNumber);
    }

    override fun getSource(): Flux<HttpLink> {
        return Flux.fromIterable(httpLinkService.findAll())
    }

    override fun processSource(sourceFlux: Flux<HttpLink>): Flux<*> {
        return sourceFlux
            .flatMap({ httpLink ->
                performHttpMethod(httpLink.url, createUriSpec(httpLink.method))
                    .map { successful -> httpLinkStatusService.updateStatusOfHttpLink(successful, httpLink) }
            }, 5)
            .doOnNext { httpLinksStatus -> logger.info(httpLinksStatus.toString()) }
    }

    private fun createUriSpec(method: SupportedHttpMethod): WebClient.RequestHeadersUriSpec<*> {
        return when (method) {
            SupportedHttpMethod.GET -> webClient.get()
            SupportedHttpMethod.HEAD -> webClient.head()
        }
    }

    private fun performHttpMethod(url: String, requestUriSpec: WebClient.RequestHeadersUriSpec<*>): Mono<Boolean> {
        return requestUriSpec.uri(url)
            .retrieve()
            .bodyToMono(String::class.java)
            .timeout(Duration.ofMillis(kryServiceWatcherConfig.serviceTimeOutInMs))
            .map { true }
            .onErrorResume { ex ->
                // TODO get response code check against expected successful code
                logger.warn(ex.message)
                Mono.just(false)
            }
    }

    @PostConstruct
    fun runPeriodically() {
        logger.info("Running Kry Watcher Periodically now...")
        this.createPeriodicTask().subscribe()
    }
}
