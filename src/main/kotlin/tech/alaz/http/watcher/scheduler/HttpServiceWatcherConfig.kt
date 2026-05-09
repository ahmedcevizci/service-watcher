package tech.alaz.http.watcher.scheduler

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "http-service-watcher.cfg.scheduler")
data class HttpServiceWatcherConfig(
     var initialWaitInMs: Long = 0,
     var runIntervalInMs: Long = 0,
     var serviceTimeOutInMs: Long = 0
)
