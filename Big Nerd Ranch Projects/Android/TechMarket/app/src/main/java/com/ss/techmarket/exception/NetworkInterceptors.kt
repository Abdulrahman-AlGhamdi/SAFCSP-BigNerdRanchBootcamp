package com.ss.techmarket.exception

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.net.HttpURLConnection
import kotlin.jvm.Throws

class NetworkInterceptors : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())

        when (response.code) {
            HttpURLConnection.HTTP_BAD_REQUEST -> throw BadRequestException()
            HttpURLConnection.HTTP_GATEWAY_TIMEOUT -> throw GatewayTimeOutException()
            HttpURLConnection.HTTP_CLIENT_TIMEOUT -> throw  ClientTimeOutException()
            HttpURLConnection.HTTP_UNAUTHORIZED -> throw UnauthorizedException()
            HttpURLConnection.HTTP_INTERNAL_ERROR -> throw ServerInternalError()
        }

        return response
    }
}