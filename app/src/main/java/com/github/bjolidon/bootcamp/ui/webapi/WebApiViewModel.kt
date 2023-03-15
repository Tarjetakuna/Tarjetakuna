package com.github.bjolidon.bootcamp.ui.webapi

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.bjolidon.bootcamp.R
import com.github.bjolidon.bootcamp.utils.Utils.Companion.isNetworkAvailable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class WebApiViewModel : ViewModel() {

    // api results
    private val _apiResults = MutableLiveData<String>()
    val apiResults: LiveData<String> = _apiResults

    fun setApiResults(s: String) {
        _apiResults.value = s
    }

    // error code
    private val _apiError = MutableLiveData<Pair<Int, String>>()
    val apiError: LiveData<Pair<Int, String>> = _apiError

    fun setError(resId: Int, s: String) {
        _apiError.value = Pair(resId, s)
    }

    // to get cards information
    fun getCards(context: Context) {
        // check if network is available
        if (isNetworkAvailable(context)) {
            getCardsWeb()
        }
//        else {
//            getBoredInformationCached()
//        }
    }

    private fun getCardsWeb() {
        val magicApi = WebApi.getMagicApi()

        // making the request
        magicApi.getCards().enqueue(object : Callback<DataCards> {
            override fun onResponse(call: Call<DataCards>, response: Response<DataCards>) {
                // Handle the response
                if (response.isSuccessful) {
                    val cards = response.body()
                    if (cards != null) {
                        setApiResults(cards.toString())
//                        saveBoredActivity(boredActivity)
                    } else {
                        setError(R.string.txt_error_msg, "no data")
                    }
                } else {
                    setError(R.string.txt_error_msg, response.errorBody().toString())
                }
            }

            override fun onFailure(call: Call<DataCards>, t: Throwable) {
                // Handle the error
                if (t.message != null) {
                    setError(R.string.txt_error_msg, t.message!!)
                } else {
                    setError(R.string.txt_error_msg, "unknown error")
                }
            }
        })
    }
}