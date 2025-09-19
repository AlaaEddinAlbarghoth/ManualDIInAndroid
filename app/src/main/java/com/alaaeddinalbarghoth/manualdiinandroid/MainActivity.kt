package com.alaaeddinalbarghoth.manualdiinandroid

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.alaaeddinalbarghoth.manualdiinandroid.app.App
import com.alaaeddinalbarghoth.manualdiinandroid.ui.theme.ManualDIInAndroidTheme

class MainActivity : ComponentActivity() {

    // Ask composition root for what we need
    private val car by lazy { (application as App).container.gasCar() }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ManualDIInAndroidTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    val msg = car.drive()
                    Toast.makeText(LocalContext.current, msg, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
