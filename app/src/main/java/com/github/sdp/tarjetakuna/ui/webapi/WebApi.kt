package com.github.sdp.tarjetakuna.ui.webapi

import com.github.sdp.tarjetakuna.ui.webapi.magicApi.MagicApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

open class WebApi {

    open var magicUrl = "https://api.magicthegathering.io/v1/"
    private lateinit var magicApi: MagicApi

    companion object WebApiObject : WebApi()

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
