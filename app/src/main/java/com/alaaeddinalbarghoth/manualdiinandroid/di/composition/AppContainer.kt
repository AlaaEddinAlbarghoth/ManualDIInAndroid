package com.alaaeddinalbarghoth.manualdiinandroid.di.composition

import android.app.Application
import com.alaaeddinalbarghoth.manualdiinandroid.di.abstractions.Car
import com.alaaeddinalbarghoth.manualdiinandroid.di.abstractions.ElectricEngine
import com.alaaeddinalbarghoth.manualdiinandroid.di.abstractions.GasEngine

// Composition Root
class AppContainer(app: Application) {

    // Singleton: Example of a singleton dependency
    val gasEngine = GasEngine()
    val electricEngine = ElectricEngine()

    // Factory: New car each time, reusing the singleton Engine
    fun gasCar() = Car(gasEngine)
    fun electricCar() = Car(electricEngine)
}
