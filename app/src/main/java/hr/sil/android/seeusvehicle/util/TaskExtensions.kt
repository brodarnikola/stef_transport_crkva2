package hr.sil.android.seeusvehicle.util

import com.google.android.gms.tasks.Task
import java.lang.RuntimeException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * @author mfatiga
 */
suspend fun <T> Task<T>.awaitForResult(): Task<T> {
    return suspendCoroutine<Task<T>> { c ->
        this@awaitForResult.addOnCompleteListener { c.resume(it) }
        this.addOnFailureListener { c.resumeWithException(it) }
        this.addOnCanceledListener { c.resumeWithException(RuntimeException("Cancelled!")) }
    }
}