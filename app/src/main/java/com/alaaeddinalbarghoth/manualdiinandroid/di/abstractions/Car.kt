package com.alaaeddinalbarghoth.manualdiinandroid.di.abstractions

class Car (private val engine: Engine) {

    fun drive() : String = "${engine.run()} -> Car is Moving!"
}