package mszcz.universe.attendancecheck

import android.app.Application
import mszcz.universe.attendancecheck.koin.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AttendanceApp: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AttendanceApp)
            modules(appModule)
        }
    }
}