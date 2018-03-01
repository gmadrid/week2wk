package com.scrawlsoft.week2wk.common

import android.app.Application
import com.scrawlsoft.week2wk.dagger.AppComponent
import com.scrawlsoft.week2wk.dagger.AppModule
import com.scrawlsoft.week2wk.dagger.DaggerAppComponent

class W2WApp : Application() {
    lateinit var appComponent: AppComponent

    private fun createAppComponent(): AppComponent =
            DaggerAppComponent.builder()
                    .appModule(AppModule(this))
                    .build()

    override fun onCreate() {
        super.onCreate()
        appComponent = createAppComponent()
    }
}