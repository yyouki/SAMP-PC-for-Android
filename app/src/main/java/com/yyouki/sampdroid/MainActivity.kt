package com.yyouki.sampdroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.yyouki.sampdroid.ui.HomeScreen
import com.yyouki.sampdroid.ui.theme.SampDroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SampDroidTheme {
                HomeScreen()
            }
        }
    }
}
