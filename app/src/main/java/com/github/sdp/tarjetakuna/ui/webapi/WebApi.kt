package com.github.sdp.tarjetakuna.ui.webapi

import com.github.sdp.tarjetakuna.ui.webapi.magicApi.MagicApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Singleton class to access the Web API of magic the gathering
 */
open class WebApi {

    /**
     * Base url of the API
     */
    open var magicUrl = "https://api.magicthegathering.io/v1/"

    /**
     * Magic API
     */
    private lateinit var magicApi: MagicApi

    companion object WebApiObject : WebApi()

    /**
     * Get the Magic API
     */
    fun getMagicApi(): MagicApi {
        if (!::magicApi.isInitialized) {
            magicApi = buildMagicApi()
        }
        return magicApi
    }

    /**
     * Build the Magic API
     */
    private fun buildMagicApi(): MagicApi {
        // building request to API to get bored information
        val retrofit = Retrofit.Builder()
            .baseUrl(magicUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(MagicApi::class.java)
    }
}
