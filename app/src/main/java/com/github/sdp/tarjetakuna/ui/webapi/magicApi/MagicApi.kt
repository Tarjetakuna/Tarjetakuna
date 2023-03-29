package com.github.sdp.tarjetakuna.ui.webapi.magicApi

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interface for the MagicApi
 */
interface MagicApi {
    @GET("cards")
    fun getCards(): Call<MagicCards>

    @GET("cards")
    fun getCardsBySet(@Query("set") setCode: String): Call<MagicCards>

    @GET("cards")
    fun getCardsByName(@Query("name") name: String): Call<MagicCards>

    @GET("cards/{id}")
    fun getCardById(@Query("multiverseId") id: String): Call<MagicCard>

    @GET("sets")
    fun getSets(): Call<MagicSets>

    @GET("sets/{code}")
    fun getSetByCode(@Query("setCode") code: String): Call<MagicSet>

}
