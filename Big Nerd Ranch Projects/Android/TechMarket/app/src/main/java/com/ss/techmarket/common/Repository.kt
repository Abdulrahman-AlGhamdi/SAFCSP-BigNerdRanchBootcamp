package com.ss.techmarket.common

import android.content.Context
import androidx.preference.PreferenceManager
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.GsonBuilder
import com.ss.techmarket.exception.*
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "Repository"

class Repository(
    applicationContext: Context,
    retrofit: Retrofit
) {

    private val tmInterface = retrofit.create(TMInterface::class.java)

    private val sharedPreferences = PreferenceManager
        .getDefaultSharedPreferences(applicationContext)

    val productListLiveData: MutableLiveData<ProductResponse> =
        MutableLiveData()

    val productDetailsLiveData: MutableLiveData<Product> =
        MutableLiveData()

    val categoriesListLiveData: MutableLiveData<CategoryResponse> =
        MutableLiveData()

    val categoryDetailsLiveData: MutableLiveData<Category> =
        MutableLiveData()

    val tagsListLiveData: MutableLiveData<TagResponse> =
        MutableLiveData()

    val tagDetailsLiveData: MutableLiveData<Tag> =
        MutableLiveData()

    private val networkListeners = mutableListOf<NetworkListener>()

    /**
     * Search products by title parameter, filtered by
     * priceMin? to priceMax?, category, and/or tag.
     * Sort by title, price, created_at, or updated_at.
     * View sort by asc or desc (Ascending or Descending) order.
     **/
    suspend fun searchProduct(
        page: Int,
        title: String? = null,
        priceMin: Double? = null,
        priceMax: Double? = null,
        category: Int? = null,
        tag: Int? = null,
        sort: Sort? = null,
        asc: Asc? = null,
        region: Int? = null,
        city: Int? = null
    ) {
        try {
            val response = tmInterface.productsSearch(
                page,
                title,
                priceMin,
                priceMax,
                category,
                tag,
                sort?.value(),
                asc?.value(),
                region,
                city
            )
            productListLiveData.postValue(response)
        } catch (e: Throwable) {
            handleNetworkError(e)
        }
    }

    suspend fun getProducts(
        page: Int,
        title: String? = null,
        priceMin: Double? = null,
        priceMax: Double? = null,
        category: Int? = null,
        tag: Int? = null,
        sort: Sort? = null,
        asc: Asc? = null,
        region: Int? = null,
        city: Int? = null
    ): ProductResponse? {
        return try {
            tmInterface.productsSearch(
                page,
                title,
                priceMin,
                priceMax,
                category,
                tag,
                sort?.value(),
                asc?.value(),
                region,
                city
            )
        } catch (e: Throwable) {
            handleNetworkError(e)
            null
        }
    }

    suspend fun getProductDetails(id: Int) {
        try {
            val product = tmInterface.getProductDetails(id)
            productDetailsLiveData.postValue(product)
        } catch (e: Throwable) {
            handleNetworkError(e)
        }
    }

    suspend fun getAllCategories() {
        try {
            val response = tmInterface.getAllCategories()
            categoriesListLiveData.postValue(response)
        } catch (e: Throwable) {
            handleNetworkError(e)
        }
    }

    suspend fun getAllTags() {
        try {
            val response = tmInterface.getAllTags()
            tagsListLiveData.postValue(response)
        } catch (e: Throwable) {
            handleNetworkError(e)
        }
    }

    suspend fun getCategoryDetails(id: Int) {
        try {
            val category = tmInterface.getCategoryDetails(id)
            categoryDetailsLiveData.postValue(category)
        } catch (e: Throwable) {
            handleNetworkError(e)
        }
    }

    suspend fun getTagDetails(id: Int) {
        try {
            val tag = tmInterface.getTagDetails(id)
            tagDetailsLiveData.postValue(tag)
        } catch (e: Throwable) {
            handleNetworkError(e)
        }
    }

    suspend fun autocompleteTag(searchQuery: String): TagResponse? {
        return try {
            tmInterface.autocompleteTag(searchQuery)
        } catch (e: Throwable) {
            handleNetworkError(e)
            null
        }
    }

    suspend fun createPost(requestBody: RequestBody) {
        try {
            tmInterface.createPost(requestBody)
        } catch (e: Throwable) {
            handleNetworkError(e)
        }
    }

    suspend fun updatePost(id: Int, requestBody: RequestBody) {
        try {
            tmInterface.updatePost(id, requestBody)
        } catch (e: Throwable) {
            handleNetworkError(e)
        }
    }

    suspend fun autocompleteCity(
        region: Int?,
        nameAR: String?,
        nameEN: String?
    ): CityResponse? {
        return try {
            tmInterface.autocompleteCity(
                region,
                nameAR,
                nameEN
            )
        } catch (e: Throwable) {
            handleNetworkError(e)
            null
        }
    }

    suspend fun getCity(
        id: Int
    ): City? {
        return try {
            tmInterface.getCity(id)
        } catch (e: Throwable) {
            handleNetworkError(e)
            null
        }
    }

    suspend fun allRegions(): RegionResponse? {
        return try {
            tmInterface.allRegions()
        } catch (e: Throwable) {
            handleNetworkError(e)
            null
        }
    }

    suspend fun getRegion(id: Int): Region? {
        return try {
            tmInterface.getRegion(id)
        } catch (e: Throwable) {
            handleNetworkError(e)
            null
        }
    }

    private fun handleNetworkError(e: Throwable) {
        when (e) {
            is BadRequestException ->
                notifyNetworkListeners(BadRequestException.code)

            is GatewayTimeOutException ->
                notifyNetworkListeners(GatewayTimeOutException.code)

            is ClientTimeOutException ->
                notifyNetworkListeners(ClientTimeOutException.code)

            is UnauthorizedException ->
                notifyNetworkListeners(UnauthorizedException.code)

            is ServerInternalError ->
                notifyNetworkListeners(ServerInternalError.code)

            else ->
                Log.d(TAG, "ERROR($e)")
        }
    }

    private fun notifyNetworkListeners(code: Int) {
        for (listener in networkListeners) {
            when (code) {
                BadRequestException.code ->
                    listener.onBadRequest()

                GatewayTimeOutException.code ->
                    listener.onGatewayTimeOut()

                ClientTimeOutException.code ->
                    listener.onClientTimeOut()

                UnauthorizedException.code ->
                    listener.onUnauthorized()

                ServerInternalError.code ->
                    listener.onInternalError()
            }
        }
    }

    fun addNetworkListener(listener: NetworkListener) {
        networkListeners += listener
    }

    fun removeNetworkListener(listener: NetworkListener) {
        networkListeners -= listener
    }

    companion object {
        private const val TM_ENDPOINT = "http://10.20.40.75/api/"

        //private const val API_KEY = ""
        private const val CLOUDINARY_NAME = "null-tm"
        private const val CLOUDINARY_API_KEY = "813594371262769"
        private const val CLOUDINARY_API_SECRET = "3Rk-ORQVKWtiV7NBHD3RNKXEcEo"

        val config: HashMap<String, String> = hashMapOf(
            "cloud_name" to CLOUDINARY_NAME,
            "api_key" to CLOUDINARY_API_KEY,
            "api_secret" to CLOUDINARY_API_SECRET
        )

        private var repository: Repository? = null

        fun initialize(applicationContext: Context) {
            if (repository == null) {
                val gson = GsonBuilder().create()

                val loggingInterceptor = HttpLoggingInterceptor()
                loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

                val client = OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .addInterceptor(NetworkInterceptors())
                    //.addInterceptor(requestInterceptor)
                    .build()

                val retrofit = Retrofit.Builder()
                    .baseUrl(TM_ENDPOINT)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()

                repository = Repository(applicationContext, retrofit)
            }
        }

        fun get(): Repository {
            return repository
                ?: throw IllegalStateException("Repository must be initialized")
        }

        private val requestInterceptor: Interceptor = Interceptor { chain ->
            val request: Request = chain.request().newBuilder()
                .build()

            chain.proceed(request)
        }
    }
}