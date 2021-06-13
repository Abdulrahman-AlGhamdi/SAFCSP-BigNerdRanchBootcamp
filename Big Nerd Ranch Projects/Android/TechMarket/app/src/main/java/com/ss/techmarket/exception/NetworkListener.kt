package com.ss.techmarket.exception

interface NetworkListener {
    fun onBadRequest()
    fun onGatewayTimeOut()
    fun onClientTimeOut()
    fun onUnauthorized()
    fun onInternalError()
}