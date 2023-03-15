package com.github.bjolidon.bootcamp.ui.webapi


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object WebApi {

    var url = "https://api.magicthegathering.io/v1"

    fun getMagicApi(): MagicApi {
        // building request to API to get bored information
        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(MagicApi::class.java)
    }
}
