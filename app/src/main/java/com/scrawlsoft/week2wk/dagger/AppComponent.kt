package com.scrawlsoft.week2wk.dagger

import com.scrawlsoft.week2wk.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(target: MainActivity)
}