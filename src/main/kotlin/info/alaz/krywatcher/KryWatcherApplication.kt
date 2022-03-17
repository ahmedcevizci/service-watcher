package info.alaz.krywatcher

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KryWatcherApplication

fun main(args: Array<String>) {
	runApplication<KryWatcherApplication>(*args)
}
