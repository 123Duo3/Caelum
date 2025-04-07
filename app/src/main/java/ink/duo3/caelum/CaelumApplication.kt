package ink.duo3.caelum

import android.app.Application
import ink.duo3.caelum.di.module
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class CaelumApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@CaelumApplication)
            modules(module)
        }
    }
}
