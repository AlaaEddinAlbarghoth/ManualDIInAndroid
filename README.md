# Manual DI in Android — Car & Engine

A tiny Android project that demonstrates **manual Dependency Injection** (no Hilt/Dagger) using the classic `Car` ← `Engine` example.
The app uses **constructor injection** and a single **composition root** (`AppContainer`) to wire the object graph.

---

## Overview

- **Constructor Injection** keeps classes pure and testable (`Car` receives an `Engine`).
- **Composition Root** centralizes wiring at app startup (`AppContainer`, created in `Application`).
- **No Service Locator**: consumers don’t fetch globals; they receive dependencies explicitly.
- Shows **two engine implementations** (Gas/Electric) and simple **factory** methods to create cars.

---

## Project Structure

```
app/src/main/java/com/alaaeddinalbarghoth/manualdiinandroid/
├─ app/
│  └─ App.kt                       # Application class (register in AndroidManifest)
├─ di/
│  ├─ abstractions/
│  │  ├─ Engine.kt                 # interface Engine { fun run(): String }
│  │  ├─ GasEngine.kt
│  │  ├─ ElectricEngine.kt
│  │  └─ Car.kt                    # class Car(private val engine: Engine) { fun drive(): String }
│  └─ composition/
│     └─ AppContainer.kt           # composition root (creates & wires graph)
├─ ui/theme/...                    # default template
└─ MainActivity.kt                 # uses the container to get a Car
```

> Your packages can differ—keep files in the same relative places.

---

## Key Classes (snippets)

**Engine & Implementations**
```kotlin
// Engine.kt
package com.alaaeddinalbarghoth.manualdiinandroid.di.abstractions

interface Engine { fun run(): String }

// GasEngine.kt
package com.alaaeddinalbarghoth.manualdiinandroid.di.abstractions

class GasEngine : Engine {
    override fun run() = "Gas engine roaring..."
}

// ElectricEngine.kt
package com.alaaeddinalbarghoth.manualdiinandroid.di.abstractions

class ElectricEngine : Engine {
    override fun run() = "Electric engine humming..."
}
```

**Car**
```kotlin
// Car.kt
package com.alaaeddinalbarghoth.manualdiinandroid.di.abstractions

class Car(private val engine: Engine) {
    fun drive(): String = "${engine.run()} -> Car is Moving!"
}
```

**Composition Root with Two Engines**
```kotlin
// AppContainer.kt
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
```

**Application (create the container once)**  
Use either `by lazy` (recommended) or `lateinit`—see the section below.
```kotlin
// App.kt
package com.alaaeddinalbarghoth.manualdiinandroid.app

import android.app.Application
import com.alaaeddinalbarghoth.manualdiinandroid.di.composition.AppContainer

class App : Application() {
    // Created on first access; main-thread only in Android startup
    val container: AppContainer by lazy(LazyThreadSafetyMode.NONE) { AppContainer(this) }
}
```
_AndroidManifest.xml_
```xml
<application
    android:name=".app.App"
    ... />
```

**Activity usage**
```kotlin
// MainActivity.kt
package com.alaaeddinalbarghoth.manualdiinandroid

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.alaaeddinalbarghoth.manualdiinandroid.app.App
import com.alaaeddinalbarghoth.manualdiinandroid.di.composition.EngineKind

class MainActivity : AppCompatActivity() {

    private val container by lazy { (application as App).container }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val eco = container.car(EngineKind.ELECTRIC)
        val msg = eco.drive()
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}
```

---

## Scopes (Lifetimes)

Manual DI = **you decide lifetimes**:

- **App scope (singleton)**: objects held inside `AppContainer` as `val`/`by lazy` (e.g., `gasEngine`).
- **Activity scope**: keep an object as a property in an Activity.
- **ViewModel scope**: create once inside a `ViewModel` and keep it until VM is cleared.
- **Transient (factory)**: create a fresh instance each time you call a factory (e.g., `car()` returns a new `Car`).

> Rule of thumb: who **owns the reference** decides the scope. Don’t put anything holding an `Activity`/`Fragment` `Context` into app singletons—use `applicationContext` only.

---

## Tests

Use **local unit tests** for `Car` and `Engine` (they don’t need Android).

### 1) Add dependencies

**Kotlin DSL** (`app/build.gradle.kts`):
```kotlin
dependencies {
    testImplementation("junit:junit:4.13.2")
    // Or, if you prefer kotlin.test:
    // testImplementation(kotlin("test"))
    // testImplementation(kotlin("test-junit"))
}
```

### 2) Create folders and file

### 3) Sample test
```kotlin
package com.alaaeddinalbarghoth.manualdiinandroid.di.abstractions

import org.junit.Assert.assertEquals
import org.junit.Test

class FakeEngine : Engine {
    override fun run() = "Fake engine started."
}

class CarTest {
    @Test
    fun drive_uses_engine_run() {
        val car = Car(FakeEngine())
        assertEquals("Fake engine started. -> Car is Moving!", car.drive())
    }
}
```


---

## Switching the Default Engine

You can pick the default at startup (by flavor, debug flag, or remote config). Example using flavors:

```kotlin
// In AppContainer.kt
val defaultEngine: Engine by lazy {
    if (BuildConfig.FLAVOR == "electric") electricEngine else gasEngine
}
```

Or toggle by `BuildConfig.DEBUG`, or route via a simple setting/feature flag.

---

## Build & Run

1. Open in Android Studio (Giraffe+).
2. Ensure Gradle sync succeeds.
3. Run the app on an emulator/device. A toast will show the `Car.drive()` message.

---

## Why manual DI?

- Zero extra tooling, full control over scopes.
- Easy to reason about and unit test.
- Great stepping stone to DI frameworks (Hilt/Dagger/Koin) once your graph grows.

---

## License

MIT — do whatever you want; a credit is appreciated.
