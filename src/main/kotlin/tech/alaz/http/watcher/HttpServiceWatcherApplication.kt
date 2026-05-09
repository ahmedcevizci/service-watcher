package tech.alaz.http.watcher

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class HttpServiceWatcherApplication

fun main(args: Array<String>) {
	runApplication<HttpServiceWatcherApplication>(*args)
}
