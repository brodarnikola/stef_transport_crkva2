package hr.sil.android.seeusvehicle

import android.app.Application
import android.content.Context
import com.testfairy.TestFairy
import hr.sil.android.seeusvehicle.backend.UserUtil
import hr.sil.android.seeusvehicle.core.remote.WSAdminConfig
import hr.sil.android.seeusvehicle.core.util.SettingsHelper
import hr.sil.android.seeusvehicle.core.util.logger
import hr.sil.android.seeusvehicle.fcm.FCMService
import org.greenrobot.eventbus.EventBus

/**
 * @author mfatiga
 */
class App : Application() {
    private val log = logger()

    companion object {
        @JvmStatic
        lateinit var ref: App
    }

    init {
        ref = this
    }

    override fun attachBaseContext(base: Context) {
        log.info("Attaching base context in APP!!")

        SettingsHelper.init(base)
        super.attachBaseContext(base)
    }

    //event bus initialization
    val eventBus: EventBus by lazy {
        EventBus.builder()
            .logNoSubscriberMessages(false)
            .sendNoSubscriberEvent(false)
            .build()
    }

    override fun onCreate() {
        super.onCreate()

        log.info("Starting...")

        //init test fairy
        TestFairy.begin(this, resources.getString(R.string.test_fairy_app_key))

        WSAdminConfig.initialize(this.applicationContext)
        WSAdminConfig.setLoginCredentials("admin", "sil2017")
    }


}