package com.github.sdp.tarjetakuna.ui.webapi

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.sdp.tarjetakuna.R
import com.github.sdp.tarjetakuna.utils.Utils.Companion.isNetworkAvailable


/**
 * ViewModel for the WebApiFragment
 */
open class WebApiViewModel : ViewModel() {

    // api results
    private val _apiResults = MutableLiveData<String>()
    val apiResults: LiveData<String> = _apiResults

    private fun setApiResults(s: String) {
        _apiResults.value = s
    }

    // error code
    private val _apiError = MutableLiveData<Pair<Int, String>>()
    val apiError: LiveData<Pair<Int, String>> = _apiError

    private fun setError(resId: Int, s: String) {
        _apiError.value = Pair(resId, s)
    }

    /**
     * Get all cards information
     */
    fun getRandomCard(context: Context) {
        // check if network is available
        if (isNetworkAvailable(context)) {
            getRandomCardWeb()
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
    protected fun getRandomCardWeb() {
        WebApi.getRandomCard()
            .thenAccept {
                setApiResults(it.toString())
            }
            .exceptionally { e ->
                setError(R.string.txt_error_msg, e.message ?: "Unknown error")
                Log.e("WebApiViewModel", "Error getting cards", e)
                null
            }
    }

    /**
     * search cards by name from the web
     */
    protected fun getCardsByNameWeb(name: String) {
        WebApi.getCardsByName(name)
            .thenAccept {
                setApiResults(it.toString())
            }
            .exceptionally { e ->
                setError(R.string.txt_error_msg, e.message ?: "Unknown error")
                Log.e("WebApiViewModel", "Error getting cards by name", e)
                null
            }
    }

    /**
     * search cards by set code from the web
     */
    protected fun getCardsBySetWeb(set: String) {
        WebApi.getCardsBySet(set)
            .thenAccept {
                setApiResults(it.toString())
            }
            .exceptionally { e ->
                setError(R.string.txt_error_msg, e.message ?: "Unknown error")
                Log.e("WebApiViewModel", "Error getting cards by set", e)
                null
            }
    }

    /**
     * get sets from the web
     */
    protected fun getSetsWeb() {
        WebApi.getSets()
            .thenAccept {
                setApiResults(it.toString())
            }
            .exceptionally { e ->
                setError(R.string.txt_error_msg, e.message ?: "Unknown error")
                Log.e("WebApiViewModel", "Error getting sets", e)
                null
            }
    }

    /**
     * get a single set by code from the web
     */
    protected fun getSetByCodeWeb(code: String) {
        WebApi.getSetByCode(code)
            .thenAccept {
                setApiResults(it.toString())
            }
            .exceptionally { e ->
                setError(R.string.txt_error_msg, e.message ?: "Unknown error")
                Log.e("WebApiViewModel", "Error getting set by code", e)
                null
            }
    }
}
