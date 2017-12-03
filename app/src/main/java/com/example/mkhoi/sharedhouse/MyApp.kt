package com.example.mkhoi.sharedhouse

import android.app.Application
import com.example.mkhoi.sharedhouse.dagger.AppModule
import com.example.mkhoi.sharedhouse.dagger.DaggerMyAppComponent
import com.example.mkhoi.sharedhouse.dagger.MyAppComponent


class MyApp: Application() {

    companion object {
        lateinit var component: MyAppComponent
    }

    override fun onCreate() {
        super.onCreate()
        component = DaggerMyAppComponent
                .builder()
                .appModule(AppModule(this))
                .build()
        component.inject(this)
    }
}