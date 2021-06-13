package com.ss.techmarket

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.util.CoilUtils
import com.cloudinary.android.LogLevel
import com.cloudinary.android.MediaManager
import com.cloudinary.android.policy.GlobalUploadPolicy
import com.cloudinary.android.signed.Signature
import com.cloudinary.android.signed.SignatureProvider
import com.ss.techmarket.common.Repository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.*
import kotlin.collections.HashMap

class TMApplication: Application(), ImageLoaderFactory {

    private lateinit var loggingInterceptor: HttpLoggingInterceptor

    override fun onCreate() {
        super.onCreate()
        Repository.initialize(applicationContext)
        loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        MediaManager.setLogLevel(LogLevel.DEBUG)

        MediaManager.init(applicationContext, Repository.config)
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(applicationContext)
            .crossfade(true)
            .okHttpClient {
                OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .cache(CoilUtils.createDefaultCache(applicationContext))
                    .build()
            }
            .build()
    }
}