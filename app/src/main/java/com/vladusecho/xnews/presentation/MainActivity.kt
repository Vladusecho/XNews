package com.vladusecho.xnews.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.vladusecho.xnews.presentation.screen.MainScreen
import com.vladusecho.xnews.ui.theme.XNewsTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            XNewsTheme {
                MainScreen()
            }
        }
    }
}
