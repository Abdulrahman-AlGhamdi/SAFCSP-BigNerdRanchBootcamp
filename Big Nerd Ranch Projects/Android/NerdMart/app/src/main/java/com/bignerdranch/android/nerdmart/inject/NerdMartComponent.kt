package com.bignerdranch.android.nerdmart.inject

import com.bignerdranch.android.nerdmart.NerdMartApplication
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    InjectionModule::class,
    NerdMartApplicationModule::class
])
interface NerdMartComponent : AndroidInjector<NerdMartApplication> {
    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<NerdMartApplication>()
}