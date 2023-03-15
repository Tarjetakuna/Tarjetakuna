package com.github.bjolidon.bootcamp.ui.webapi

import retrofit2.Call
import retrofit2.http.GET

interface MagicApi {
    @GET("cards")
    fun getCards(): Call<DataCards>
}
