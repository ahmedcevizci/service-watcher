package tech.alaz.http.watcher.scheduler

interface CancellableTask {
    fun isCancelled(): Boolean
    fun cancel()
}
