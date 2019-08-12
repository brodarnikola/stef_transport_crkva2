package hr.sil.android.seeusvehicle.core.connectivity

import hr.sil.android.seeusvehicle.core.util.logger
import hr.sil.android.seeusvehicle.nina.Nina
import kotlinx.coroutines.*
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * @author mfatiga
 */
object NinaChecker {
    private const val checkPeriod: Long = 1_000L
    private val log = logger()

    private val listeners = ConcurrentHashMap<String, (Boolean) -> Unit>()
    fun addListener(listener: (Boolean) -> Unit): String {
        val key = UUID.randomUUID().toString()
        listeners[key] = listener

        //immediately call new listener with last check result
        lastResult?.let { listener.invoke(it) }

        if (listeners.size == 1) {
            log.info("Checking if Nina is ready")
            runChecker()
        }
        return key
    }

    fun removeListener(key: String) {
        listeners.remove(key)
        if (listeners.size == 0) {
            checkerJob?.cancel()
        }
    }

    private fun notifyListeners(state: Boolean) {
        listeners.forEach { it.value.invoke(state) }
    }

    private var lastResult: Boolean? = null
    private var checkerJob: Job? = null

    private fun doCheck() {
        val isNinaReady = Nina.isReady()
        if (lastResult != isNinaReady) {
            lastResult = isNinaReady
            notifyListeners(isNinaReady)
        }
    }

    private fun runChecker() {
        checkerJob = GlobalScope.launch {
            while (isActive) {
                doCheck()
                delay(checkPeriod)
            }
        }
    }
}