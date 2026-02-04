package com.vladusecho.xnews.di

import android.content.Context
import com.vladusecho.xnews.presentation.activity.MainActivity
import com.vladusecho.xnews.presentation.viewModel.ViewModelFactory
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(
    modules = [
        DataModule::class,
        ViewModelModule::class
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

    fun mainViewModelFactory(): ViewModelFactory
}