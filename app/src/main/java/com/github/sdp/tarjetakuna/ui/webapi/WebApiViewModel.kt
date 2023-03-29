package com.github.sdp.tarjetakuna.ui.webapi

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.sdp.tarjetakuna.R
import com.github.sdp.tarjetakuna.utils.Utils.Companion.isNetworkAvailable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * ViewModel for the WebApiFragment
 */
open class WebApiViewModel : ViewModel() {

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
        else {
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
        else {
            Toast.makeText(context, "No network available", Toast.LENGTH_SHORT).show()
        }
    }

    // to get cards information from the web
    protected fun getCardsWeb() {
        ApiCall(WebApi.getMagicApi().getCards(), this@WebApiViewModel).enqueue()
    }

    protected fun getSetsWeb() {
        ApiCall(WebApi.getMagicApi().getSets(), this@WebApiViewModel).enqueue()
    }

    open class ApiCall<T>(private val call: Call<T>, private val viewModel: WebApiViewModel) {
        fun enqueue() {
            call.enqueue(object : ApiCallHandler<T>(viewModel) {})
        }
    }

    open class ApiCallHandler<T>(private val viewModel: WebApiViewModel) : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            CallbackHandler<T>().handleResponse(response, viewModel)
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            CallbackHandler<T>().handleFailure(t, viewModel)
        }
    }

    class CallbackHandler<T> {
        public fun handleResponse(response: Response<T>, viewModel: WebApiViewModel) {
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
                val error = response.errorBody()!!.string()
                viewModel.setError(R.string.txt_error_msg, error)
            }
        }

        fun handleFailure(t: Throwable, viewModel: WebApiViewModel) {
            // Handle the error
            if (t.message != null) {
                viewModel.setError(R.string.txt_error_msg, t.message!!)
            } else {
                viewModel.setError(R.string.txt_error_msg, "unknown error")
            }
        }
    }
}
