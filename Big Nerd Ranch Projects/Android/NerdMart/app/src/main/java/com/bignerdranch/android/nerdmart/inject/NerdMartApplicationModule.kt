package com.bignerdranch.android.nerdmart.inject

import android.content.Context
import com.bignerdranch.android.nerdmart.NerdMartApplication
import com.bignerdranch.android.nerdmart.model.DataStore
import com.bignerdranch.android.nerdmartservice.service.NerdMartService
import com.bignerdranch.android.nerdmartservice.service.NerdMartServiceInterface
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers


@Module
class NerdMartApplicationModule {
    @Provides
    fun provideNerdMartServiceInterface(): NerdMartServiceInterface = NerdMartService()

    @Provides
    fun provideContext(application: NerdMartApplication) = application as Context

    @Provides
    fun provideCart(dataStore: DataStore) = dataStore.cachedCart

    @Provides
    fun provideUser(dataStore: DataStore) = dataStore.cachedUser

    @Provides
    fun provideObservationScheduler(): Scheduler = AndroidSchedulers.mainThread()
}