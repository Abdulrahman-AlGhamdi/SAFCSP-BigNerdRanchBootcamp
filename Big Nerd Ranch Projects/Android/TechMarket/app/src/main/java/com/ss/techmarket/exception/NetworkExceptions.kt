package com.ss.techmarket.exception

import java.io.IOException
import java.net.HttpURLConnection

class BadRequestException : IOException() {
    companion object {
        const val code = HttpURLConnection.HTTP_BAD_REQUEST
    }
}

class GatewayTimeOutException : IOException() {
    companion object {
        const val code = HttpURLConnection.HTTP_GATEWAY_TIMEOUT
    }
}

class ClientTimeOutException : IOException() {
    companion object {
        const val code = HttpURLConnection.HTTP_CLIENT_TIMEOUT
    }
}

class UnauthorizedException : IOException() {
    companion object {
        const val code = HttpURLConnection.HTTP_UNAUTHORIZED
    }
}

class ServerInternalError : IOException() {
    companion object {
        const val code = HttpURLConnection.HTTP_INTERNAL_ERROR
    }
}