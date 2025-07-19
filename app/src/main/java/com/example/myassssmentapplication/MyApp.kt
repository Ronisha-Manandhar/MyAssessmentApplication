// MyApp.kt
package com.example.myassssmentapplication

import android.app.Application
import com.example.myassssmentapplication.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

/**
 *
 * Custom Application class.
 * Initializes Koin for dependency injection on app startup.
 */
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Start Koin with the application context and our modules
        startKoin {
            androidContext(this@MyApp)
            modules(appModule)
        }
    }
}
