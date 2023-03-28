package com.github.sdp.tarjetakuna.ui.filter

import android.content.DialogInterface
import android.os.Bundle
import android.widget.AdapterView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.sdp.tarjetakuna.R
import com.github.sdp.tarjetakuna.model.MagicCard
import com.github.sdp.tarjetakuna.model.MagicLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


/**
 * ViewModel for the FilterCardsFragment
 */
class FilterCardsViewModel : ViewModel() {

    private var valuesMap: Map<Int, ArrayList<String>> = emptyMap()

    private lateinit var filteredCards: ArrayList<MagicCard>
    private lateinit var filter: Filter

    private lateinit var cards: ArrayList<MagicCard>

    private val textViewId = MutableLiveData<Int>()
    private val newMessage = MutableLiveData<String>()
    val mediatorLiveData = MediatorLiveData<Pair<Int?, String?>>().apply {
        var value1: Int? = null
        var value2: String? = null

        addSource(textViewId) {
            value1 = it
            if (value1 != null && value2 != null) {
                value = Pair(value1, value2)
            }
        }

        addSource(newMessage) {
            value2 = it
            if (value1 != null && value2 != null) {
                value = Pair(value1, value2)
            }
        }
    }

    // The names that will be displayed in the filter
    val layoutArray: Array<String> = MagicLayout.values().map { it.toString() }.toTypedArray()
    val namesArray = arrayOf(
        "Meandering Towershell",
        "Angel of Mercy",
        "Angel of Serenity",
        "Angel of Sanctions",
        "Angel of the Dire Hour"
    )
    val manaCostArray: Array<String> = (0..16).map { it.toString() }.toTypedArray()

    /**
     * Initialize the attributes of the view model
     */
    fun initializeAttributes(bundle: Bundle) {
        cards = getCardsFromBundle(bundle)
        filteredCards = ArrayList()
    }


    /**
     * Get the cards from the intent
     */
    private fun getCardsFromBundle(bundle: Bundle): ArrayList<MagicCard> {
        val gson = Gson()
        val cardsJson = bundle.getString("cards")
        val type: Type = object : TypeToken<List<MagicCard?>?>() {}.type
        return gson.fromJson(cardsJson, type)
    }

    /**
     * Put the cards in the filtered cards array if they match the filter
     */
    fun filterCards() {
        filter = convertToFilter()

        filteredCards.clear()
        for (card in cards) {
            if (filter.doesContain(card)) {
                filteredCards.add(card)
            }
        }
    }

    /**
     * Convert the map of selected values to a filter object
     * TODO add some filters here when we know which one we really want
     */
    private fun convertToFilter(): Filter {
        var name = ""
        val layout: ArrayList<MagicLayout> = arrayListOf()
        val convertedManaCost: ArrayList<Int> = arrayListOf()
        for (i in valuesMap.keys) {
            when (i) {
                R.id.cardNameTextView -> {
                    name = valuesMap[i]!![0]
                }

                R.id.layoutTextView -> {
                    for (j in valuesMap[i]!!) {
                        layout.add(MagicLayout.valueOf(j))
                    }
                }

                R.id.cmcTextView -> {
                    for (j in valuesMap[i]!!) {
                        convertedManaCost.add(j.toInt())
                    }
                }
            }
        }
        return Filter(name, layout, convertedManaCost)
    }


    /**
     * Operations that happen when the choices are clicked
     */
    fun multiChoiceOnChoicesClicked(
        index: Int,
        boolean: Boolean,
        selectedItemsPositions: ArrayList<Int>
    ) {
        if (boolean) {
            selectedItemsPositions.add(index)
            selectedItemsPositions.sort()
        } else {
            selectedItemsPositions.remove(Integer.valueOf(index))
        }
    }


    /**
     * Set the "OK" button so that it sets the text on the text view and adds the values to the valuesMap
     */
    fun multiChoiceOKButtonClicked(
        textViewId: Int, selectedItemsPositions: ArrayList<Int>,
        itemsArray: Array<String>
    ) {
        // create list to add them in valuesMap
        val selectedItems: ArrayList<String> = ArrayList()
        val stringBuilder = StringBuilder()

        for (index in selectedItemsPositions.indices) {
            stringBuilder.append(itemsArray[selectedItemsPositions[index]])
            selectedItems.add(itemsArray[selectedItemsPositions[index]])
            if (index != (selectedItemsPositions.size - 1)) {
                stringBuilder.append(", ")
            }
        }
        if (selectedItems.isNotEmpty()) {
            valuesMap = valuesMap.plus(Pair(textViewId, selectedItems))
        }

        // set text on textView and the message for the view to update it
        this.textViewId.value = textViewId
        newMessage.value = stringBuilder.toString()
    }


    /**
     * What happens when the "OK" button is clicked
     */
    fun setSingleOKButton(
        dialog: DialogInterface,
        textViewId: Int,
        options: Array<String>
    ) {
        val selectedItemPosition = (dialog as AlertDialog).listView.checkedItemPosition
        if (selectedItemPosition != AdapterView.INVALID_POSITION) {
            valuesMap =
                valuesMap.plus(Pair(textViewId, arrayListOf(options[selectedItemPosition])))
            // set text on textView and set ellipses so that it does not exceed the box
            this.textViewId.value = textViewId
            newMessage.value = options[selectedItemPosition]
        }
    }

    /**
     * Set the "Clear All" button so that it clears the text view and the valuesMap
     */
    fun setClearAllButton(textViewId: Int) {
        valuesMap = valuesMap.minus(textViewId)
    }


    /**
     * Check if the valuesMap contains the key title and the item "item"
     */
    fun valuesMapDoesContain(id: Int, item: String): Boolean {
        return valuesMap.containsKey(id) && valuesMap[id]!!.contains(item)
    }

    /**
     * Get the already selected item if there is any, otherwise return -1
     * This method is used for the single choice dialog
     */

    fun getAlreadySelectedItem(itemsArray: Array<String>, textViewId: Int): Int {
        var selectedItem = -1
        for (i in itemsArray.indices) {
            if (valuesMapDoesContain(textViewId, itemsArray[i])) {
                selectedItem = i
            }
        }
        return selectedItem
    }

    /**
     * Get the already selected items if there is any, otherwise return an empty array
     * This method is used for the multi choice dialog
     */
    fun getAlreadySelectedItems(itemsArray: Array<String>, textViewId: Int): BooleanArray {
        val selectedItems = BooleanArray(itemsArray.size)
        for (i in itemsArray) {
            if (valuesMapDoesContain(textViewId, i)) {
                selectedItems[itemsArray.indexOf(i)] = true
            }
        }
        return selectedItems
    }

    /**
     * Get the filtered cards in string format
     */
    fun getFilterCardsString(): String {
        return filteredCards.toString()
    }
}
