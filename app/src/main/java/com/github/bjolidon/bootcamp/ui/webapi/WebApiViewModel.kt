package com.github.bjolidon.bootcamp.ui.webapi

import android.content.Context
import android.widget.Toast
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
        // TODO else get from cache
        else{
            Toast.makeText(context, "No network available", Toast.LENGTH_SHORT).show()
        }
    }

    // to get sets information
    fun getSets(context: Context) {
        // check if network is available
        if (isNetworkAvailable(context)) {
            getSetsWeb()
        }
        // TODO else get from cache
        else{
            Toast.makeText(context, "No network available", Toast.LENGTH_SHORT).show()
        }
    }

    // to get cards information from the web
    private fun getCardsWeb() {

        // getting the api
        val magicApi = WebApi.getMagicApi()

        // making the request
        magicApi.getCards().enqueue(object : Callback<DataCards> {
            override fun onResponse(call: Call<DataCards>, response: Response<DataCards>) {
                CallbackHandler<DataCards>().handleResponse(response, this@WebApiViewModel)
            }

            override fun onFailure(call: Call<DataCards>, t: Throwable) {
                CallbackHandler<DataCards>().handleFailure(t, this@WebApiViewModel)
            }
        })
    }

    private fun getSetsWeb() {
        // getting the api
        val magicApi = WebApi.getMagicApi()

        // making the request
        magicApi.getSets().enqueue(object : Callback<DataSets> {
            override fun onResponse(call: Call<DataSets>, response: Response<DataSets>) {
                CallbackHandler<DataSets>().handleResponse(response, this@WebApiViewModel)
            }

            override fun onFailure(call: Call<DataSets>, t: Throwable) {
                CallbackHandler<DataSets>().handleFailure(t, this@WebApiViewModel)
            }
        })
    }

    class CallbackHandler<T>{
        public fun handleResponse(response: Response<T>, viewModel: WebApiViewModel){
            // Handle the response
            if (response.isSuccessful) {
                val sets = response.body()
                if (sets != null) {
                    viewModel.setApiResults(sets.toString())
                    // TODO save to cache
                } else {
                    viewModel.setError(R.string.txt_error_msg, "no data")
                }
            } else {
                viewModel.setError(R.string.txt_error_msg, response.errorBody().toString())
            }
        }

        fun handleFailure(t: Throwable, viewModel: WebApiViewModel){
            // Handle the error
            if (t.message != null) {
                viewModel.setError(R.string.txt_error_msg, t.message!!)
            } else {
                viewModel.setError(R.string.txt_error_msg, "unknown error")
            }
        }
    }
}


