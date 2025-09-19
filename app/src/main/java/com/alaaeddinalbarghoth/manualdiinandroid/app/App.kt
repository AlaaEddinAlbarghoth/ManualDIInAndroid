package com.alaaeddinalbarghoth.manualdiinandroid.app

import android.app.Application
import com.alaaeddinalbarghoth.manualdiinandroid.di.composition.AppContainer

class App : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()

        container = AppContainer(this)
    }
}
