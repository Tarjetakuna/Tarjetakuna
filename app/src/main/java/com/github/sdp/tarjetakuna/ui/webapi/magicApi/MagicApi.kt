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
    fun getCards(): Call<MagicApiCards>

    @GET("cards")
    fun getCardsBySet(@Query("set") setCode: String): Call<MagicApiCards>

    @GET("cards")
    fun getCardsByName(@Query("name") name: String): Call<MagicApiCards>

    @GET("cards/{multiverseId}")
    fun getCardById(@Path("multiverseId") multiverseId: String): Call<MagicApiCard>

    @GET("sets")
    fun getSets(): Call<MagicApiSets>

    @GET("sets/{setCode}")
    fun getSetByCode(@Path("setCode") setCode: String): Call<MagicApiSet>
}
