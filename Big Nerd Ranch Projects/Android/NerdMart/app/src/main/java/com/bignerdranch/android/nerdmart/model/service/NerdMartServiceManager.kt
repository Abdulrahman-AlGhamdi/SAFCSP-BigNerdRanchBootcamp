package com.bignerdranch.android.nerdmart.model.service

import com.bignerdranch.android.nerdmart.model.DataStore
import com.bignerdranch.android.nerdmartservice.service.NerdMartServiceInterface
import com.bignerdranch.android.nerdmartservice.service.payload.Cart
import com.bignerdranch.android.nerdmartservice.service.payload.Product
import com.bignerdranch.android.nerdmartservice.service.payload.User
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.annotations.Async
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NerdMartServiceManager @Inject constructor(
        private val serviceInterface: NerdMartServiceInterface,
        private val dataStore: DataStore,
        private val observationScheduler: Scheduler
) {
    private fun <T> Observable<T>.defaultSchedulers(): Observable<T> {
        return subscribeOn(Schedulers.io())
                .observeOn(observationScheduler)
    }

    fun authenticate(username: String, password: String): Observable<Boolean> {
        return serviceInterface.authenticate(username, password)
                .doOnNext { dataStore.cachedUser = it }
                .map { it != User.NO_USER }
                .defaultSchedulers()
    }

    private fun getToken(): Observable<UUID> =
            Observable.just(dataStore.cachedAuthToken)

    fun getProducts(): Observable<Product> {
        return getToken()
                .flatMap { serviceInterface.requestProducts(it) }
                .doOnNext { dataStore.cachedProducts = it }
                .flatMap { Observable.fromIterable(it) }
                .defaultSchedulers()
    }

    fun getCart(): Observable<Cart> {
        return getToken()
                .flatMap { serviceInterface.fetchUserCart(it) }
                .doOnNext { dataStore.cachedCart = it }
                .defaultSchedulers()
    }

    fun postProductToCart(product: Product): Observable<Boolean> {
        return getToken()
                .flatMap { serviceInterface.addProductToCart(it, product) }
                .defaultSchedulers()
    }

    fun signout(): Observable<Boolean> {
        dataStore.clearCache()
        return serviceInterface.signout()
    }
}