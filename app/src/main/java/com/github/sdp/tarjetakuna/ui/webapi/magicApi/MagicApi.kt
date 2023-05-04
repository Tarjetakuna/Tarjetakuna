package com.github.sdp.tarjetakuna.ui.webapi.magicApi

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Interface for the MagicApi
 */
interface MagicApi {
    @GET("cards/random")
    fun getRandomCard(): Call<MagicApiCard>

    @GET("cards/search")
    fun getCardsBySearch(@Query("q") search: String, @Query("order") order: String): Call<MagicApiCards>

    @GET("sets")
    fun getSets(): Call<MagicApiSets>

    @GET("sets/{setCode}")
    fun getSetByCode(@Path("setCode") setCode: String): Call<MagicApiSet>
}
