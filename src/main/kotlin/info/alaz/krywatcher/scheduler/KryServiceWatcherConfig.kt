package info.alaz.krywatcher.scheduler

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "kry.cfg.scheduler")
data class KryServiceWatcherConfig(
     var initialWaitInMs: Long = 0,
     var runIntervalInMs: Long = 0,
     var serviceTimeOutInMs: Long = 0
)
