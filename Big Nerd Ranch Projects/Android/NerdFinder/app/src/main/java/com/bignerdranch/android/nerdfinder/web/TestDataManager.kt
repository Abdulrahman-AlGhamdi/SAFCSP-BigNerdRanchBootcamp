package com.bignerdranch.android.nerdfinder.web

import com.bignerdranch.android.nerdfinder.model.TokenStore
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit

class TestDataManager private constructor(
        tokenStore: TokenStore,
        retrofit: Retrofit,
        authenticatedRetrofit: Retrofit
) : DataManager(tokenStore, retrofit, authenticatedRetrofit) {

    override val observeOnScheduler: Scheduler
        get() = Schedulers.trampoline()
    override val subscribeOnScheduler: Scheduler
        get() = Schedulers.trampoline()


    companion object {
        fun getInstance(
                tokenStore: TokenStore,
                retrofit: Retrofit,
                authenticatedRetrofit: Retrofit
        ): DataManager {
            if (dataManager == null) {
                dataManager = TestDataManager(tokenStore, retrofit, authenticatedRetrofit)
            }
            return dataManager!!
        }

        fun reset() {
            dataManager = null
        }
    }
}