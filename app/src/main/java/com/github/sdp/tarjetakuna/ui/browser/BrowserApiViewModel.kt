package com.github.sdp.tarjetakuna.ui.browser

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.sdp.tarjetakuna.R
import com.github.sdp.tarjetakuna.model.MagicCard
import com.github.sdp.tarjetakuna.ui.webapi.WebApi
import com.github.sdp.tarjetakuna.utils.Utils

class BrowserApiViewModel : ViewModel() {

    // api results
    private val _apiResults = MutableLiveData<String>()
    val apiResults: LiveData<String> = _apiResults

    private val cardList = MutableLiveData<ArrayList<MagicCard>>()
    val cardListLiveData: LiveData<ArrayList<MagicCard>> = cardList

    private fun setCardList(list: ArrayList<MagicCard>) {
        cardList.value = list
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
        if (Utils.isNetworkAvailable(context)) {
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
        if (Utils.isNetworkAvailable(context)) {
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
        if (Utils.isNetworkAvailable(context)) {
            getCardsBySetWeb(setCode)
        }
        // TODO else get from cache
        else {
            Toast.makeText(context, "No network available", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * get cards from the web
     */
    private fun getRandomCardWeb() {
        WebApi.getRandomCard()
            .thenAccept {
                setCardList(arrayListOf(it.toMagicCard()))
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
    private fun getCardsByNameWeb(name: String) {
        WebApi.getCardsByName(name)
            .thenAccept {
                setCardList(ArrayList(it.data.map { magicApiCard -> magicApiCard.toMagicCard() }))
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
    private fun getCardsBySetWeb(set: String) {
        WebApi.getCardsBySet(set)
            .thenAccept {
                setCardList(ArrayList(it.data.map { magicApiCard -> magicApiCard.toMagicCard() }))
            }
            .exceptionally { e ->
                setError(R.string.txt_error_msg, e.message ?: "Unknown error")
                Log.e("WebApiViewModel", "Error getting cards by set", e)
                null
            }
    }

}
