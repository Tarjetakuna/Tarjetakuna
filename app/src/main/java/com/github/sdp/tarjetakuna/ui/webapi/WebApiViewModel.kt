package com.github.sdp.tarjetakuna.ui.webapi

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sdp.tarjetakuna.R
import com.github.sdp.tarjetakuna.database.local.LocalDatabaseProvider
import com.github.sdp.tarjetakuna.ui.webapi.magicApi.MagicApiCards
import com.github.sdp.tarjetakuna.ui.webapi.magicApi.MagicApiSets
import com.github.sdp.tarjetakuna.utils.Utils.Companion.isNetworkAvailable
import kotlinx.coroutines.launch


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

    // Cards and sets live data
    private val _cards = MutableLiveData<MagicApiCards>()
    val cards: LiveData<MagicApiCards> = _cards

    private val _sets = MutableLiveData<MagicApiSets>()
    val sets: LiveData<MagicApiSets> = _sets

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
            Toast.makeText(
                context,
                "No network available, retrieving from cache",
                Toast.LENGTH_SHORT
            ).show()
            viewModelScope.launch {
                val cards =
                    LocalDatabaseProvider.getDatabase(LocalDatabaseProvider.CARDS_DATABASE_NAME)
                        ?.apiMagicCardDao()?.getAllCards()
                if (cards != null)
                    _cards.value = MagicApiCards(cards)
                else {
                    setError(R.string.txt_error_msg, "Cache is empty")
                }
            }


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
            Toast.makeText(
                context,
                "No network available, retrieving from cache",
                Toast.LENGTH_SHORT
            ).show()

            viewModelScope.launch {
                val cards =
                    LocalDatabaseProvider.getDatabase(LocalDatabaseProvider.CARDS_DATABASE_NAME)
                        ?.apiMagicCardDao()?.getCardsByName(cardName)
                if (cards != null)
                    _cards.value = MagicApiCards(cards)
                else {
                    setError(R.string.txt_error_msg, "Cache is empty")
                }
            }
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
            Toast.makeText(
                context,
                "No network available, retrieving from cache",
                Toast.LENGTH_SHORT
            ).show()

            viewModelScope.launch {
                val cards =
                    LocalDatabaseProvider.getDatabase(LocalDatabaseProvider.CARDS_DATABASE_NAME)
                        ?.apiMagicCardDao()?.getCardsBySet(setCode)
                if (cards != null)
                    _cards.value = MagicApiCards(cards)
                else {
                    setError(R.string.txt_error_msg, "Cache is empty")
                }
            }

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
            Toast.makeText(
                context,
                "No network available, retrieving from cache",
                Toast.LENGTH_SHORT
            ).show()

            viewModelScope.launch {
                val card =
                    LocalDatabaseProvider.getDatabase(LocalDatabaseProvider.CARDS_DATABASE_NAME)
                        ?.apiMagicCardDao()?.getCardById(id)
                if (card != null)
                    _cards.value = MagicApiCards(listOf(card))
                else {
                    setError(R.string.txt_error_msg, "Cache is empty")
                }
            }
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
            Toast.makeText(
                context,
                "No network available, retrieving from cache",
                Toast.LENGTH_SHORT
            ).show()

//            viewModelScope.launch {
//                val sets =
//                    LocalDatabaseProvider.getDatabase(LocalDatabaseProvider.API_CARDS_DATABASE_NAME)
//                        ?.apiMagicCardDao()?.getAllSets()
//                if (sets != null)
//                    _sets.value = MagicApiSets(sets)
//                else {
//                    setError(R.string.txt_error_msg, "Cache is empty")
//                }
//            }
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
        WebApi.getCards()
            .thenAccept {
//                if (it.cards.isNotEmpty()) {
//                    viewModelScope.launch {
//                        LocalDatabaseProvider.getDatabase(LocalDatabaseProvider.CARDS_DATABASE_NAME)
//                            ?.apiMagicCardDao()?.insertCards(it.cards)
//                    }
//                }
                _cards.value = it
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
                _cards.value = it
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
                _cards.value = it
                setApiResults(it.toString())
            }
            .exceptionally { e ->
                setError(R.string.txt_error_msg, e.message ?: "Unknown error")
                Log.e("WebApiViewModel", "Error getting cards by set", e)
                null
            }
    }

    /**
     * get a single card by id from the web
     */
    protected fun getCardByIdWeb(id: String) {
        WebApi.getCardById(id)
            .thenAccept {
                if (it != null) _cards.value = MagicApiCards(listOf(it.card))
                else _cards.value = MagicApiCards(listOf())
                setApiResults(it.toString())
            }
            .exceptionally { e ->
                setError(R.string.txt_error_msg, e.message ?: "Unknown error")
                Log.e("WebApiViewModel", "Error getting card by id", e)
                null
            }
    }

    /**
     * get sets from the web
     */
    protected fun getSetsWeb() {
        WebApi.getSets()
            .thenAccept {
                _sets.value = it
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
                if (it != null) _sets.value = MagicApiSets(listOf(it.set))
                else _sets.value = MagicApiSets(listOf())
                setApiResults(it.toString())
            }
            .exceptionally { e ->
                setError(R.string.txt_error_msg, e.message ?: "Unknown error")
                Log.e("WebApiViewModel", "Error getting set by code", e)
                null
            }
    }
}
