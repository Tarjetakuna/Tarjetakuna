package com.github.sdp.tarjetakuna.ui.webapi

import com.github.sdp.tarjetakuna.ui.webapi.magicApi.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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
    open var magicUrl: String = "https://api.magicthegathering.io/v1/"
        set(value) {
            field = value
            _magicApi = null
        }


    /**
     * readTimeout for the API (in seconds), default is 30 as some query take a long time
     */
    private var readTimeout = 30L

    /**
     * Magic API
     */
    private var _magicApi: MagicApi? = null
    private val magicApiProperty get() = _magicApi!!


    companion object WebApiObject : WebApi()

    /**
     * Get the Magic API
     */
    private fun getMagicApi(): MagicApi {
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

    /**
     * Get the cards
     */
    fun getCards(): CompletableFuture<MagicApiCards> {
        val promise = CompletableFuture<MagicApiCards>()
        ApiCall(getMagicApi().getCards(), promise).enqueue()
        return promise
    }

    /**
     * Get the cards by [set] code
     */
    fun getCardsBySet(set: String): CompletableFuture<MagicApiCards> {
        val promise = CompletableFuture<MagicApiCards>()
        ApiCall(getMagicApi().getCardsBySet(set), promise).enqueue()
        return promise
    }

    /**
     * Get the cards by [name]
     */
    fun getCardsByName(name: String): CompletableFuture<MagicApiCards> {
        val promise = CompletableFuture<MagicApiCards>()
        ApiCall(getMagicApi().getCardsByName(name), promise).enqueue()
        return promise
    }

    /**
     * Get the cards by [id]
     */
    fun getCardById(id: String): CompletableFuture<MagicApiCard> {
        val promise = CompletableFuture<MagicApiCard>()
        ApiCall(getMagicApi().getCardById(id), promise).enqueue()
        return promise
    }

    /**
     * Get the sets
     */
    fun getSets(): CompletableFuture<MagicApiSets> {
        val promise = CompletableFuture<MagicApiSets>()
        ApiCall(getMagicApi().getSets(), promise).enqueue()
        return promise
    }

    /**
     * Get the set by [code]
     */
    fun getSetByCode(code: String): CompletableFuture<MagicApiSet> {
        val promise = CompletableFuture<MagicApiSet>()
        ApiCall(getMagicApi().getSetByCode(code), promise).enqueue()
        return promise
    }

    class ApiCall<T>(private val call: Call<T>, private val promise: CompletableFuture<T>) {
        fun enqueue() {
            call.enqueue(object : ApiCallHandler<T>(promise) {})
        }
    }

    open class ApiCallHandler<T>(private val promise: CompletableFuture<T>) : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            CallbackHandler<T>().handleResponse(response, promise)
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            CallbackHandler<T>().handleFailure(t, promise)
        }
    }

    class CallbackHandler<T> {
        fun handleResponse(response: Response<T>, promise: CompletableFuture<T>) {
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    promise.complete(body)
                } else {
                    promise.completeExceptionally(Exception("body is null"))
                }
            } else {
                val error = response.errorBody()!!.string()
                promise.completeExceptionally(Exception(error))
            }
        }

        fun handleFailure(t: Throwable, promise: CompletableFuture<T>) {
            val error = t.message ?: "unknown error"
            promise.completeExceptionally(Exception(error))
        }
    }

}
