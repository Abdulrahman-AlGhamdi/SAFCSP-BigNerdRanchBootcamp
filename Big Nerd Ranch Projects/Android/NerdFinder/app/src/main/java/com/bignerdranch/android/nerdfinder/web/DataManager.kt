package com.bignerdranch.android.nerdfinder.web

import android.content.Context
import android.net.Uri
import android.util.Log
import com.bignerdranch.android.nerdfinder.exception.AuthorizationInterceptor
import com.bignerdranch.android.nerdfinder.exception.UnauthorizedException
import com.bignerdranch.android.nerdfinder.listener.VenueCheckInListener
import com.bignerdranch.android.nerdfinder.listener.VenueSearchListener
import com.bignerdranch.android.nerdfinder.model.TokenStore
import com.bignerdranch.android.nerdfinder.model.Venue
import com.bignerdranch.android.nerdfinder.model.VenueSearchResponse
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

open class DataManager constructor(
        private val tokenStore: TokenStore,
        private val retrofit: Retrofit,
        private val authenticatedRetrofit: Retrofit
) {

    open val observeOnScheduler: Scheduler
        get() = AndroidSchedulers.mainThread()
    open val subscribeOnScheduler: Scheduler
        get() = Schedulers.io()

    var venueList = emptyList<Venue>()
        private set

    private val searchListenerList = mutableListOf<VenueSearchListener>()
    private val checkInListenerList = mutableListOf<VenueCheckInListener>()

    fun fetchVenueSearch() {
        val venueInterface: VenueInterface = retrofit.create(VenueInterface::class.java)
        venueInterface.venueSearch(TEST_LAT_LNG)
                .enqueue(object : Callback<VenueSearchResponse> {
                    override fun onResponse(
                            call: Call<VenueSearchResponse>,
                            response: Response<VenueSearchResponse>
                    ) {
                        venueList = response.body()?.venues ?: emptyList()
                        notifySearchListeners()
                    }

                    override fun onFailure(call: Call<VenueSearchResponse>, t: Throwable) {
                        Log.e(TAG, "Failed to fetch venue search", t)
                    }
                })
    }

    fun getVenue(venueId: String): Venue? =
            venueList.find { it.id == venueId }

    fun getAuthenticationUrl(): String? {
        return Uri.parse(OAUTH_ENDPOINT).buildUpon()
                .appendQueryParameter("client_id", CLIENT_ID)
                .appendQueryParameter("response_type", "token")
                .appendQueryParameter("redirect_uri", OAUTH_REDIRECT_URI)
                .build()
                .toString()
    }

    fun addVenueSearchListener(listener: VenueSearchListener) {
        searchListenerList += listener
    }

    fun removeVenueSearchListener(listener: VenueSearchListener) {
        searchListenerList -= listener
    }

    private fun notifySearchListeners() {
        for (listener in searchListenerList) {
            listener.onVenueSearchFinished()
        }
    }

    fun addVenueCheckInListener(listener: VenueCheckInListener) {
        checkInListenerList += listener
    }

    fun removeVenueCheckInListener(listener: VenueCheckInListener) {
        checkInListenerList -= listener
    }

    private fun notifyCheckInListeners() {
        for (listener in checkInListenerList) {
            listener.onVenueCheckInFinished()
        }
    }

    private fun notifyCheckInListenersTokenExpired() {
        for (listener in checkInListenerList) {
            listener.onTokenExpired()
        }
    }

    fun checkInToVenue(venueId: String) {
        val venueInterface = authenticatedRetrofit.create(VenueInterface::class.java)
        venueInterface.venueCheckIn(venueId)
                .observeOn(observeOnScheduler)
                .subscribeOn(subscribeOnScheduler)
                .subscribe(
                        { result -> notifyCheckInListeners() },
                        { error -> handleCheckInException(error) }
                )
    }
    private fun handleCheckInException(error: Throwable) {
        if (error is UnauthorizedException) {
            tokenStore.accessToken = null
            notifyCheckInListenersTokenExpired()
        }
    }

    companion object {
        private const val TAG = "DataManager"
        private const val FOURSQUARE_ENDPOINT = "https://api.foursquare.com/v2/"
        private const val OAUTH_ENDPOINT = "https://foursquare.com/oauth2/authenticate"
        const val OAUTH_REDIRECT_URI = "https://bignerdranch.com/"
        private const val CLIENT_ID = "DFCHQUG22QWDW0INACTB5HFEUSBA5SQ05VBCSWRRFFDRKWAN"
        private const val CLIENT_SECRET = "GRMWPCMKSYLWWGOIVBPJUBST2T4FYOQ2YCVAKOJAAVRTCNS2"
        private const val FOURSQUARE_VERSION = "20150406"
        private const val FOURSQUARE_MODE = "foursquare"
        private const val SWARM_MODE = "swarm"
        private const val TEST_LAT_LNG = "33.759,-84.332"

        var dataManager: DataManager? = null
        private lateinit var        tokenStore: TokenStore

        fun initialize(context: Context) {
            if (dataManager == null) {
                val gson: Gson = GsonBuilder()
                        .registerTypeAdapter(
                                VenueSearchResponse::class.java,
                                VenueListDeserializer())
                        .create()

                val loggingInterceptor = HttpLoggingInterceptor()
                loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

                val client = OkHttpClient.Builder()
                        .addInterceptor(loggingInterceptor)
                        .addInterceptor(requestInterceptor)
                        .build()

                val retrofit = Retrofit.Builder()
                        .baseUrl(FOURSQUARE_ENDPOINT)
                        .client(client)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build()

                val authenticatedClient = OkHttpClient.Builder()
                        .addInterceptor(loggingInterceptor)
                        .addInterceptor(AuthorizationInterceptor())
                        .addInterceptor(authenticatedRequestInterceptor)
                        .build()

                val authenticatedRetrofit = Retrofit.Builder()
                        .baseUrl(FOURSQUARE_ENDPOINT)
                        .client(authenticatedClient)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .build()

                tokenStore = TokenStore.getInstance(context)
                dataManager = DataManager(tokenStore, retrofit, authenticatedRetrofit)
            }
        }

        fun get(): DataManager {
            return dataManager
                    ?: throw IllegalStateException("DataManager must be initialized")
        }

        private val requestInterceptor: Interceptor = Interceptor { chain ->
            val url: HttpUrl = chain.request().url().newBuilder()
                    .addQueryParameter("client_id", CLIENT_ID)
                    .addQueryParameter("client_secret", CLIENT_SECRET)
                    .addQueryParameter("v", FOURSQUARE_VERSION)
                    .addQueryParameter("m", FOURSQUARE_MODE)
                    .build()

            val request: Request = chain.request().newBuilder()
                    .url(url)
                    .build()

            chain.proceed(request)
        }

        private val authenticatedRequestInterceptor: Interceptor = Interceptor { chain ->
            val url: HttpUrl = chain.request().url().newBuilder()
                    .addQueryParameter("oauth_token", tokenStore.accessToken)
                    .addQueryParameter("v", FOURSQUARE_VERSION)
                    .addQueryParameter("m", SWARM_MODE)
                    .build()
            val request: Request = chain.request().newBuilder()
                    .url(url)
                    .build()
            chain.proceed(request)
        }
    }
}
