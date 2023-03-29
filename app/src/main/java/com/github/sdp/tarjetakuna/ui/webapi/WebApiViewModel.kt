package com.github.sdp.tarjetakuna.ui.webapi

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.sdp.tarjetakuna.R
import com.github.sdp.tarjetakuna.ui.webapi.magicApi.MagicCard
import com.github.sdp.tarjetakuna.ui.webapi.magicApi.MagicCards
import com.github.sdp.tarjetakuna.ui.webapi.magicApi.MagicSet
import com.github.sdp.tarjetakuna.ui.webapi.magicApi.MagicSets
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

    // Cards and sets live data
    private val _cards = MutableLiveData<MagicCards>()
    val cards: LiveData<MagicCards> = _cards

    private val _sets = MutableLiveData<MagicSets>()
    val sets: LiveData<MagicSets> = _sets

    /**
     * Get all cards information
     */
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

    /**
     * Search cards by name
     */
    fun getCardsByName(context: Context, cardName: String) {
        // check if network is available
        if (isNetworkAvailable(context)) {
            getCardsByNameWeb(cardName)
        }
        // TODO else get from cache
        else {
            Toast.makeText(context, "No network available", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Search cards by set code
     */
    fun getCardsBySet(context: Context, setCode: String) {
        // check if network is available
        if (isNetworkAvailable(context)) {
            getCardsBySetWeb(setCode)
        }
        // TODO else get from cache
        else {
            Toast.makeText(context, "No network available", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Search card by id
     */
    fun getCardById(context: Context, id: String) {
        // check if network is available
        if (isNetworkAvailable(context)) {
            getCardByIdWeb(id)
        }
        // TODO else get from cache
        else {
            Toast.makeText(context, "No network available", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Get all sets information
     */
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

    /**
     * Get a single set by its set code
     */
    fun getSetByCode(context: Context, code: String) {
        // check if network is available
        if (isNetworkAvailable(context)) {
            getSetByCodeWeb(code)
        }
        // TODO else get from cache
        else {
            Toast.makeText(context, "No network available", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * get cards from the web
     */
    protected fun getCardsWeb() {
        ApiCall(WebApi.getMagicApi().getCards(), this@WebApiViewModel).enqueue()
    }

    /**
     * search cards by name from the web
     */
    protected fun getCardsByNameWeb(name: String) {
        ApiCall(WebApi.getMagicApi().getCardsByName(name), this@WebApiViewModel).enqueue()
    }

    /**
     * search cards by set code from the web
     */
    protected fun getCardsBySetWeb(set: String) {
        ApiCall(WebApi.getMagicApi().getCardsBySet(set), this@WebApiViewModel).enqueue()
    }

    /**
     * get a single card by id from the web
     */
    protected fun getCardByIdWeb(id: String) {
        ApiCall(WebApi.getMagicApi().getCardById(id), this@WebApiViewModel).enqueue()
    }

    /**
     * get sets from the web
     */
    protected fun getSetsWeb() {
        ApiCall(WebApi.getMagicApi().getSets(), this@WebApiViewModel).enqueue()
    }

    /**
     * get a single set by code from the web
     */
    protected fun getSetByCodeWeb(code: String) {
        ApiCall(WebApi.getMagicApi().getSetByCode(code), this@WebApiViewModel).enqueue()
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
        fun handleResponse(response: Response<T>, viewModel: WebApiViewModel) {
            // Handle the response
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    if (body is MagicCards) {
                        viewModel._cards.value = body
                    } else if (body is MagicSets) {
                        viewModel._sets.value = body
                    } else if (body is MagicCard) {
                        viewModel._cards.value = MagicCards(listOf(body))
                    } else if (body is MagicSet) {
                        viewModel._sets.value = MagicSets(listOf(body))
                    }
                    viewModel.setApiResults(body.toString())
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
