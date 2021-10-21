package com.spokn.album.core.application

import android.app.Application
import com.spokn.album.core.di.AppModule
import com.spokn.album.core.di.CoreComponent
import com.spokn.album.core.di.DaggerCoreComponent
import com.spokn.album.core.networking.synk.Synk

open class CoreApp : Application() {

    companion object {
        lateinit var coreComponent: CoreComponent
    }

    override fun onCreate() {
        super.onCreate()
        initSynk()
        initDI()
    }

    private fun initSynk() {
        Synk.init(this)
    }


    private fun initDI() {
        coreComponent = DaggerCoreComponent.builder().appModule(AppModule(this)).build()
    }
}