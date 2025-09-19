package com.alaaeddinalbarghoth.manualdiinandroid.di.abstractions

import junit.framework.TestCase.assertEquals
import org.junit.Test

class FakeEngine : Engine {
    override fun run(): String {
        return "Fake engine started."
    }

}

class CarTest {

    @Test
    fun drive_uses_engine_run() {
        val car = Car(FakeEngine())
        assertEquals("Fake engine started. -> Car is Moving!", car.drive())
    }
}
