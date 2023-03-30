package com.github.sdp.tarjetakuna.ui.webapi

import com.github.sdp.tarjetakuna.ui.webapi.magicApi.MagicApi
import com.github.sdp.tarjetakuna.ui.webapi.magicApi.MagicCards
import com.github.sdp.tarjetakuna.utils.ResourceHelper.ResourceHelper
import com.google.gson.Gson
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.CompletableFuture
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
     * writeTimeout for the API
     */
    private var writeTimeout = 10L

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
            .writeTimeout(writeTimeout, TimeUnit.SECONDS)
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

    /**
     * Set the write timeout, and invalidate the api
     */
    fun setWriteTimeout(writeTimeout: Long) {
        this.writeTimeout = writeTimeout
        _magicApi = null
    }

//    fun getCards(): Future<MagicCards> {
//        val response = getMagicApi().getCards().execute()
//            if (response.isSuccessful) {
//                return@Future response.body() ?: MagicCards(emptyList())
//            }
//            return@Future MagicCards(emptyList())
//    }

    private fun getCardsFromFile(): MagicCards {
        val cardsJSON = ResourceHelper.loadString("magic_webapi_cards_response.json")
        return Gson().fromJson(cardsJSON, MagicCards::class.java)
    }

    fun getCards(): CompletableFuture<MagicCards> {
        val promise = CompletableFuture<MagicCards>()
        promise.complete(getCardsFromFile())
        return promise
    }
}
