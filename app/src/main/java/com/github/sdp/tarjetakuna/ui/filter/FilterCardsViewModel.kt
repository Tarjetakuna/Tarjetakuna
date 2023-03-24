package com.github.sdp.tarjetakuna.ui.filter

import android.content.DialogInterface
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.AdapterView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
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

    // So we are able to call the getString(resourceId) method
    private lateinit var fragment: FilterCardsFragment

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
    fun initializeAttributes(bundle: Bundle, fragment: FilterCardsFragment) {
        cards = getCardsFromBundle(bundle)
        filteredCards = ArrayList()
        this.fragment = fragment
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
        textView: TextView, selectedItemsPositions: ArrayList<Int>,
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
            valuesMap = valuesMap.plus(Pair(textView.id, selectedItems))
        }

        // set text on textView and set ellipses so that it does not exceed the box
        displayTextOnTextView(textView, stringBuilder.toString())
    }


    /**
     * What happens when the "OK" button is clicked
     */
    fun setSingleOKButton(
        dialog: DialogInterface,
        textView: TextView,
        options: Array<String>
    ) {
        val selectedItemPosition = (dialog as AlertDialog).listView.checkedItemPosition
        if (selectedItemPosition != AdapterView.INVALID_POSITION) {
            valuesMap = valuesMap.plus(Pair(textView.id, arrayListOf(options[selectedItemPosition])))
            // set text on textView and set ellipses so that it does not exceed the box
            displayTextOnTextView(textView, options[selectedItemPosition])
        }
    }

    /**
     * Set the "Clear All" button so that it clears the text view and the valuesMap
     */
    fun setClearAllButton(textView: TextView) {
        textView.text = ""
        valuesMap = valuesMap.minus(textView.id)
    }

    /**
     * Set text on textView and set ellipses so that it does not exceed the box
     */
    private fun displayTextOnTextView(textView: TextView, message: String) {
        textView.maxWidth = textView.measuredWidth
        textView.maxLines = 1
        textView.setHorizontallyScrolling(true)
        textView.movementMethod = ScrollingMovementMethod()
        textView.text = message
    }

    /**
     * Check if the valuesMap contains the key title and the item "item"
     */
    fun valuesMapDoesContain(id: Int, item: String): Boolean {
        return valuesMap.containsKey(id) && valuesMap[id]!!.contains(item)
    }

    /**
     * Get the filtered cards in string format
     */
    fun getFilterCardsString(): String {
        return filteredCards.toString()
    }
}
