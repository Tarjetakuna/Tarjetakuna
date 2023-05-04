package com.github.sdp.tarjetakuna.ui.browser

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.sdp.tarjetakuna.model.MagicCard
import com.github.sdp.tarjetakuna.ui.webapi.WebApi
import com.github.sdp.tarjetakuna.utils.Utils

class BrowserApiViewModel : ViewModel() {

    private val _cardList = MutableLiveData<ArrayList<MagicCard>>()
    val cardList: LiveData<ArrayList<MagicCard>> = _cardList

    private fun setCardList(list: ArrayList<MagicCard>) {
        _cardList.value = list
    }


    /**
     * Get all cards information
     */
    fun getRandomCard(context: Context) {
        // check if network is available
        if (Utils.isNetworkAvailable(context)) {
            getRandomCardWeb(context)
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
            getCardsByNameWeb(context, cardName)
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
            getCardsBySetWeb(context, setCode)
        }
        // TODO else get from cache
        else {
            Toast.makeText(context, "No network available", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * get cards from the web
     */
    private fun getRandomCardWeb(context: Context) {
        WebApi.getRandomCard()
            .thenAccept {
                setCardList(arrayListOf(it.toMagicCard()))
            }
            .exceptionally { e ->
                Toast.makeText(context, "No card found", Toast.LENGTH_SHORT).show()
                Log.e("WebApiViewModel", "Error getting cards", e)
                null
            }
    }

    /**
     * search cards by name from the web
     */
    private fun getCardsByNameWeb(context: Context, name: String) {
        WebApi.getCardsByName(name)
            .thenAccept {
                setCardList(ArrayList(it.data.map { magicApiCard -> magicApiCard.toMagicCard() }))
            }
            .exceptionally { e ->
                Toast.makeText(context, "No card found", Toast.LENGTH_SHORT).show()
                Log.e("WebApiViewModel", "Error getting cards by name", e)
                null
            }
    }

    /**
     * search cards by set code from the web
     */
    private fun getCardsBySetWeb(context: Context, set: String) {
        WebApi.getCardsBySet(set)
            .thenAccept {
                setCardList(ArrayList(it.data.map { magicApiCard -> magicApiCard.toMagicCard() }))
            }
            .exceptionally { e ->
                Toast.makeText(context, "No card found", Toast.LENGTH_SHORT).show()
                Log.e("WebApiViewModel", "Error getting cards by set", e)
                null
            }
    }

}
