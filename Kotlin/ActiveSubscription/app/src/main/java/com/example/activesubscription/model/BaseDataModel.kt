package com.example.activesubscription.model

import java.io.Serializable

abstract class IJRPaytmDataModel :
    Serializable {
    private var networkResponse: NetworkResponse? = null
    fun setNetworkResponse(var1: NetworkResponse?) {
        networkResponse = var1
    }

    fun getNetworkResponse(): NetworkResponse? {
        return networkResponse
    }

    @Throws(Exception::class)
    fun parseResponse(var1: String?, var2: Gson): IJRPaytmDataModel {
        return var2.fromJson(var1, this.javaClass)
    }
}