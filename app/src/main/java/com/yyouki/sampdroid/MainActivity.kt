package com.yyouki.sampdroid

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import com.yyouki.sampdroid.ui.HomeScreen
import com.yyouki.sampdroid.ui.theme.SampDroidTheme

private const val PREFERENCES_NAME = "sampdroid_settings"
private const val KEY_DARK_THEME = "dark_theme"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Read the saved preference once, then persist only when the user changes it.
        val preferences = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE)
        val initialDarkTheme = preferences.getBoolean(KEY_DARK_THEME, true)

        setContent {
            var isDarkTheme by remember { mutableStateOf(initialDarkTheme) }

            SampDroidTheme(darkTheme = isDarkTheme) {
                val backgroundColor = androidx.compose.material3.MaterialTheme.colorScheme.background

                // Keep system bars readable when switching between light and dark palettes.
                SideEffect {
                    window.statusBarColor = backgroundColor.toArgb()
                    window.navigationBarColor = backgroundColor.toArgb()
                    window.decorView.systemUiVisibility = if (isDarkTheme) {
                        0
                    } else {
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or
                            View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                    }
                }

                HomeScreen(
                    isDarkTheme = isDarkTheme,
                    onToggleTheme = {
                        isDarkTheme = !isDarkTheme
                        preferences.edit().putBoolean(KEY_DARK_THEME, isDarkTheme).apply()
                    },
                )
            }
        }
    }
}
