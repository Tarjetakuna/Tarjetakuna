package com.github.sdp.tarjetakuna.ui.webapi

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

open class WebApiClass {
    open var magicUrl = "https://api.magicthegathering.io/v1/"
    private lateinit var magicApi: MagicApi

    fun getMagicApi(): MagicApi {
        if (!::magicApi.isInitialized) {
            magicApi = buildMagicApi()
        }
        return magicApi
    }

    private fun buildMagicApi(): MagicApi {
        // building request to API to get bored information
        val retrofit = Retrofit.Builder()
            .baseUrl(magicUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(MagicApi::class.java)
    }
}
