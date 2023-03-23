package com.github.sdp.tarjetakuna.ui.webapi

import retrofit2.Call
import retrofit2.http.GET

interface MagicApi {
    @GET("cards")
    fun getCards(): Call<DataCards>

    @GET("sets")
    fun getSets(): Call<DataSets>
}
