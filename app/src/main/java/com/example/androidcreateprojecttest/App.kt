package com.example.androidcreateprojecttest

import android.app.Application
import com.example.androidcreateprojecttest.util.SharedPreferencesUtil
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        application = this
    }

    companion object {
        lateinit var application: Application
            private set

        var myUserID: String = ""
            get() {
                field = SharedPreferencesUtil.getUserID(application.applicationContext).orEmpty()
                return field
            }
            private set
    }
}
