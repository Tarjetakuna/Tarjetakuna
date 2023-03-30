package com.github.sdp.tarjetakuna.ui.webapi.magicApi

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
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

    @GET("cards/{multiverseId}")
    fun getCardById(@Path("multiverseId") multiverseId: String): Call<MagicCard>

    @GET("sets")
    fun getSets(): Call<MagicSets>

    @GET("sets/{setCode}")
    fun getSetByCode(@Path("setCode") setCode: String): Call<MagicSet>

}
