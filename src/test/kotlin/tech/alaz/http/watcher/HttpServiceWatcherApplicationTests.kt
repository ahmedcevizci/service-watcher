package tech.alaz.http.watcher

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@EnableJpaAuditing
@SpringBootTest
@Testcontainers
class HttpServiceWatcherApplicationTests {

    companion object {
        @Container
        @ServiceConnection
        val mysql: MySQLContainer<*> = MySQLContainer("mysql:9.2.0")
    }

    @Test
    fun contextLoads() {
    }

}
