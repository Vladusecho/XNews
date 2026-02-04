package com.vladusecho.xnews

import android.app.Application
import com.vladusecho.xnews.di.ApplicationComponent
import com.vladusecho.xnews.di.DaggerApplicationComponent

class XNewsApp : Application() {


    lateinit var appComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerApplicationComponent.factory().create(this)
    }
}