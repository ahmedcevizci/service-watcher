package info.alaz.krywatcher.scheduler

import org.slf4j.LoggerFactory
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Scheduler
import reactor.core.scheduler.Schedulers
import java.time.Duration
import java.util.concurrent.atomic.AtomicBoolean

abstract class PeriodicReactiveTask<S> @JvmOverloads constructor(// Needed due to Java's type erasure
    private val sourceClass: Class<S>,
    private val taskName: String,
    private val initialWaitInMs: Long,
    private val runIntervalInMs: Long,
    private val batchSize: Long = Long.MAX_VALUE,
    private val limitRate: Int = 8,
) : CancellableTask {

    private val logger = LoggerFactory.getLogger(javaClass)
    private val cancelled: AtomicBoolean = AtomicBoolean(false)

    protected abstract fun processInitialTasks(cycleNumber: Long): Mono<*>
    protected abstract fun getSource(): Flux<S>
    protected abstract fun processSource(sourceFlux: Flux<S>): Flux<*>

    fun createPeriodicTask(taskScheduler : Scheduler = Schedulers.newParallel(taskName)): Flux<*> {
        val cycleFlux =
            Flux.interval(Duration.ofMillis(initialWaitInMs), Duration.ofMillis(runIntervalInMs), taskScheduler)
        return buildProcessingFlux(cycleFlux)
    }

    private fun buildProcessingFlux(cyclesFlux: Flux<Long>): Flux<*> {
        return cyclesFlux.onBackpressureDrop { overlappedCycle ->
            logger.warn("$taskName: $overlappedCycle. cycle has overlapped.")
        }
            .takeUntil { isCancelled() }
            .doOnDiscard(Long::class.java)
            { cycleNumber -> logger.warn("$taskName: Skipping $cycleNumber. cycle due to cancellation.") }
            .doOnCancel { logger.warn("$taskName: Cycle has been cancelled.") }
            .doOnNext { cycleNumber -> logger.info("$taskName: $cycleNumber. cycle has started.") }
            .concatMap { cycleNumber -> handleInitialTasks(cycleNumber) }
            .concatMap { cycleNumber -> getCancellableSourceFlux(cycleNumber) }
            .publish { sourceFlux: Flux<S> -> processSource(sourceFlux) }
    }

    private fun handleInitialTasks(cycleNumber: Long): Mono<Long> {
        return processInitialTasks(cycleNumber)
            .map { cycleNumber }
            .switchIfEmpty(Mono.just(cycleNumber))
            .onErrorResume { throwable: Throwable ->
                logger.warn(
                    "$taskName: $cycleNumber. cycle: Initial tasks have thrown an error. Task will continue with main tasks.",
                    throwable
                )
                Mono.just(cycleNumber)
            }
    }

    private fun getCancellableSourceFlux(cycleNumber: Long): Flux<S> {
        return getSource()
            .take(batchSize)
            .limitRate(limitRate)
            .filter { !isCancelled() }
            // we filter out source items in order to prevent mutating actions to propagate downstream
            // and to allow already in progress messages to finish gracefully.
            .doOnDiscard(sourceClass)
            { sourceItem: S ->
                logger.warn("$taskName: $cycleNumber. cycle: Source item is being discarded due to cancellation: ${sourceItem.toString()}")
            }
    }

    override fun isCancelled(): Boolean {
        return cancelled.get()
    }

    @Synchronized
    override fun cancel() {
        if (!isCancelled()) {
            logger.warn("'$taskName' periodic task being cancelled now...")
            cancelled.set(true)
            logger.warn("'$taskName' periodic task has been cancelled. Task may still process remaining, running items in the flow.")
        }
    }
}
