package com.vladusecho.xnews.presentation.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vladusecho.xnews.XNewsApp
import com.vladusecho.xnews.presentation.screen.MainScreen
import com.vladusecho.xnews.presentation.viewModel.MainViewModel
import com.vladusecho.xnews.presentation.viewModel.ViewModelFactory
import com.vladusecho.xnews.ui.theme.XNewsTheme
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val component by lazy {
        (application as XNewsApp).appComponent
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            XNewsTheme {
                MainScreen(viewModelFactory)
            }
        }
    }
}