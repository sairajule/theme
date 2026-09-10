package com.rajule.themelauncher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import com.rajule.themelauncher.ui.home.ThemeLauncherRoot

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Home is a dead end, not a back-stack entry: swallow back presses instead of exiting.
        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() { /* no-op */ }
            }
        )

        setContent {
            MaterialTheme {
                ThemeLauncherRoot()
            }
        }
    }
}
