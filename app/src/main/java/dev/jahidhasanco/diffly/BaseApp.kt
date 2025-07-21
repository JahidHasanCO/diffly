package dev.jahidhasanco.diffly

import android.app.Application
import dev.jahidhasanco.diffly.data.local.PrefsManager
import dev.jahidhasanco.diffly.di.AppModule

class BaseApp : Application() {
    lateinit var settingsManager: PrefsManager
        private set

    override fun onCreate() {
        super.onCreate()
        AppModule.init(this)
    }
}