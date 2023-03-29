package com.github.sdp.tarjetakuna.ui.webapi

import com.github.sdp.tarjetakuna.ui.webapi.magicApi.MagicApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


/**
 * Singleton class to access the Web API of magic the gathering
 */
open class WebApi {

    /**
     * Base url of the API
     */
    open var magicUrl = "https://api.magicthegathering.io/v1/"

    /**
     * readTimeout for the API
     */
    private var readTimeout = 10L

    /**
     * Magic API
     */
    private var _magicApi: MagicApi? = null
    private val magicApiProperty get() = _magicApi!!


    companion object WebApiObject : WebApi()

    /**
     * Get the Magic API
     */
    fun getMagicApi(): MagicApi {
        if (_magicApi == null) {
            _magicApi = buildMagicApi()
        }
        return magicApiProperty
    }

    /**
     * Build the Magic API
     */
    private fun buildMagicApi(): MagicApi {

        val okHttpClient: OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(readTimeout, TimeUnit.SECONDS)
            .writeTimeout(10L, TimeUnit.SECONDS)
            .build()

        // building request to API to get bored information
        val retrofit = Retrofit.Builder()
            .baseUrl(magicUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(MagicApi::class.java)
    }

    /**
     * Set the read timeout, and invalidate the api
     */
    fun setReadTimeout(readTimeout: Long) {
        this.readTimeout = readTimeout
        _magicApi = null
    }
}
