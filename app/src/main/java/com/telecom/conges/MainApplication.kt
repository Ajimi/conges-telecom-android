package com.telecom.conges

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.telecom.conges.di.myModule
import net.danlew.android.joda.JodaTimeAndroid
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // start Koin!
        JodaTimeAndroid.init(this)
        startKoin {
            // Android context
            androidContext(this@MainApplication)
            // modules
            modules(myModule)
        }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this);

    }
}