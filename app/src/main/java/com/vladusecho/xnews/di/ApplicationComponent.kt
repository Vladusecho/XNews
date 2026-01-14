package com.vladusecho.xnews.di

import android.content.Context
import com.vladusecho.xnews.presentation.MainActivity
import com.vladusecho.xnews.presentation.MainViewModel
import com.vladusecho.xnews.presentation.MainViewModelFactory
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(
    modules = [
        DataModule::class
    ]
)
interface ApplicationComponent {

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance context: Context
        ) : ApplicationComponent
    }

    fun inject(activity: MainActivity)

    fun mainViewModelFactory(): MainViewModelFactory
}