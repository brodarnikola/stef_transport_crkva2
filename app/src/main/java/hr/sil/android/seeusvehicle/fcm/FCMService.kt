package hr.sil.android.seeusvehicle.fcm

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import hr.sil.android.seeusvehicle.App
import hr.sil.android.seeusvehicle.backend.UserUtil
import hr.sil.android.seeusvehicle.core.util.logger
import hr.sil.android.seeusvehicle.preferences.PreferenceStore
import hr.sil.android.seeusvehicle.util.ByteOperations
import hr.sil.android.seeusvehicle.util.awaitForResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

open class FCMService : FirebaseMessagingService() {
    private val log = logger()

    companion object {
        val token: String?
            get() {
                val stored = PreferenceStore.fcmToken
                return if (stored != null && stored.isNotBlank()) stored else null
            }

        suspend fun requestToken(): String? {
            val fetchedToken = try {
                val task = FirebaseInstanceId.getInstance().instanceId.awaitForResult()
                if (!task.isSuccessful) task.result?.token else null
            } catch (exc: Exception) {
                Log.e("FCMService", "Error while getting FCM token!", exc)
                null
            }

            return if (fetchedToken != null && fetchedToken.isNotBlank()) {
                PreferenceStore.fcmToken = fetchedToken
                fetchedToken
            } else {
                null
            }
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val data = remoteMessage.data.values.firstOrNull()
        if (data != null) {
            val pushNotificationByte = (Integer.parseInt(data, 2).toInt() and 0xFF).toByte()
            ByteOperations.onBackendPush(pushNotificationByte)
        }
    }

    override fun onNewToken(token: String) {
        log.info("Refreshed token: $token")
        PreferenceStore.fcmToken = token

        GlobalScope.launch(Dispatchers.Default) {
            if (!UserUtil.updateDevicePushToken(token)) {
                log.error("Error in registration to server please check your internet connection")
            }
        }
    }
}