package com.bignerdranch.android.nerdmart

import com.bignerdranch.android.nerdmart.inject.DaggerNerdMartComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import timber.log.Timber

class NerdMartApplication : DaggerApplication() {
    override fun onCreate() {
        super.onCreate()
        initializeTimber()
    }
    override fun applicationInjector(): AndroidInjector<NerdMartApplication> {
        return DaggerNerdMartComponent.builder().create(this)
    }
    private fun initializeTimber() {
        Timber.plant(Timber.DebugTree())
    }
}