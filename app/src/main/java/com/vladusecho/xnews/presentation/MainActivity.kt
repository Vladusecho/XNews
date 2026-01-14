package com.vladusecho.xnews.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vladusecho.xnews.XNewsApp
import com.vladusecho.xnews.presentation.screen.MainScreen
import com.vladusecho.xnews.ui.theme.XNewsTheme
import javax.inject.Inject
import kotlin.getValue

class MainActivity : ComponentActivity() {

    private val appComponent by lazy {
        (application as XNewsApp).appComponent
    }

    private val viewModelFactory: MainViewModelFactory by lazy {
        appComponent.mainViewModelFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            XNewsTheme {
                val viewModel: MainViewModel = viewModel(
                    factory = viewModelFactory
                )
                MainScreen(viewModel)
            }
        }
    }
}
