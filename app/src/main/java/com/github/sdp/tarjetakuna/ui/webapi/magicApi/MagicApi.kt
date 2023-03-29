package com.github.sdp.tarjetakuna.ui.webapi.magicApi

import retrofit2.Call
import retrofit2.http.GET

/**
 * Interface for the MagicApi
 */
interface MagicApi {
    @GET("cards")
    fun getCards(): Call<MagicCards>

    @GET("sets")
    fun getSets(): Call<MagicSets>
}
