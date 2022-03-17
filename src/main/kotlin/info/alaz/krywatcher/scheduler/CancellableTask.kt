package info.alaz.krywatcher.scheduler

interface CancellableTask {
    fun isCancelled(): Boolean
    fun cancel()
}
