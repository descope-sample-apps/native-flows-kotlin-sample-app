package com.descope.androiddemo

import android.app.Application
import android.content.pm.ApplicationInfo
import com.descope.Descope
import com.descope.sdk.DescopeLogger

internal const val descopeProjectId = "<YOUR-PROJECT-ID>" // set your project ID here

class DemoApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        // Descope Setup
        Descope.setup(context = this, projectId = descopeProjectId) {
 //         baseUrl = "myBaseUrl"
            val isDebuggable = 0 != applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE
            if (isDebuggable) {
                logger = DescopeLogger()
            }
        }
    }
}
