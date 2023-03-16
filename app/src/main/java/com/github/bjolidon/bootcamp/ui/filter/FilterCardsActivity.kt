package com.github.bjolidon.bootcamp.ui.filter

import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.AdapterView.INVALID_POSITION
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.github.bjolidon.bootcamp.MainActivity
import com.github.bjolidon.bootcamp.R
import com.github.bjolidon.bootcamp.model.MagicCard
import com.github.bjolidon.bootcamp.model.MagicLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import kotlin.collections.ArrayList


/**
    * Activity to filter cards.
    * Get the list of cards to be filtered from the intent as a json string
    * e.g
    * {"convertedManaCost": 7,
       "imageUrl": "https://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=149935&type=card",
       "layout": "Normal",
       "manaCost": "{5}{W}{W}",
       "name": "Test Card",
       "number": 56,
       "set": {
           "code": "MT15",
           "name": "Magic 2015"
        },
        "text": "Test Type"
      }
 */
class FilterCardsActivity : AppCompatActivity() {

    private val layoutArray: Array<String> = MagicLayout.values().map { it.toString() }.toTypedArray()
    private val namesArray = arrayOf("Meandering Towershell", "Angel of Mercy", "Angel of Serenity", "Angel of Sanctions", "Angel of the Dire Hour")
    private val manaCostArray: Array<String> = (0..16).map { it.toString() }.toTypedArray()

    private var valuesMap: Map<String, ArrayList<String>> = emptyMap()
    private lateinit var cards: ArrayList<MagicCard>
    private lateinit var filteredCards: ArrayList<MagicCard>
    private lateinit var filter: Filter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter_cards)
        title = "Filter Cards"
        // take the cards from the intent so that we can process them
        cards = getCardsFromIntent()
        filteredCards = ArrayList()

        // TODO change the UI for this filter to be either an keyboard entry or a sliding bar
        val manaCostTextView: TextView = findViewById(R.id.cmcTextView)
        addListenerToMultiChoice(manaCostTextView, getString(R.string.cmc), manaCostArray)

        val layoutTextView: TextView = findViewById(R.id.layoutTextView)
        addListenerToMultiChoice(layoutTextView, getString(R.string.layout_name), layoutArray)

        val nameTextView: TextView = findViewById(R.id.cardNameTextView)
        addListenerToSingleChoice(nameTextView, getString(R.string.card_name), namesArray)

        val applyFilterButton: Button = findViewById(R.id.filterButton)
        applyFilterButton.setOnClickListener {
            applyFilterButton.isEnabled = false
            filter = convertToFilter()
            filterCards()
            displayCardsFiltered(applyFilterButton)
        }

        val returnMainButton: Button = findViewById(R.id.returnMainButton)
        returnMainButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * Show multi choice dialog
     * inspired from https://www.geeksforgeeks.org/how-to-implement-multiselect-dropdown-in-android/
     */
    private fun showMultiChoiceDialog(textView: TextView, title: String,
                                      selectedItemsBool: BooleanArray, itemsArray: Array<String>) {

        val selectedItemsPositions: ArrayList<Int> = ArrayList()
        // add already selected items in the list of selected items
        for (index in selectedItemsBool.indices) {
            if (selectedItemsBool[index]) {
                selectedItemsPositions.add(index)
            }
        }
        val builder: AlertDialog.Builder = AlertDialog.Builder(this@FilterCardsActivity)
        builder.setTitle(title)
        builder.setCancelable(false)

        builder.setMultiChoiceItems(itemsArray, selectedItemsBool
        ) { _, index, boolean ->
            multiChoiceOnChoicesClicked(index, boolean, selectedItemsPositions)
        }

        builder.setPositiveButton("OK"
        ) { _, _ ->
            multiChoiceOKButtonClicked(textView, title, selectedItemsPositions, itemsArray)
        }

        setClearAllButton(builder, textView, title)
        builder.show()
    }

    /**
     * Operations that happen when the choices are clicked
     */
    private fun multiChoiceOnChoicesClicked(index: Int, boolean: Boolean, selectedItemsPositions: ArrayList<Int>) {
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
    private fun multiChoiceOKButtonClicked(textView: TextView, title: String,
                                           selectedItemsPositions: ArrayList<Int>, itemsArray: Array<String>) {
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
            valuesMap = valuesMap.plus(Pair(title, selectedItems))
        }

        // set text on textView and set ellipses so that it does not exceed the box
        displayTextOnTextView(textView, stringBuilder.toString())
    }

    /**
     * Set the "Clear All" button so that it clears the text view and the valuesMap
     */
    private fun setClearAllButton(builder: AlertDialog.Builder, textView: TextView, title: String) {
        builder.setNeutralButton("Clear All"
        ) { _, _ ->
            textView.text = ""
            valuesMap = valuesMap.minus(title)
        }
    }

    /**
     * Show single choice dialog
     */
    private fun showSingleChoiceDialog(textView: TextView, title: String,
                                       selectedChoice: Int, options: Array<String>) {

        val builder: AlertDialog.Builder = AlertDialog.Builder(this@FilterCardsActivity)
        builder.setTitle(title)
        builder.setCancelable(false)
        builder.setSingleChoiceItems(options, selectedChoice) { _, _ -> }

        builder.setPositiveButton("OK"
        ) { dialog, _ ->
            val selectedItemPosition = (dialog as AlertDialog).listView.checkedItemPosition
            if (selectedItemPosition != INVALID_POSITION) {
                valuesMap = valuesMap.plus(Pair(title, arrayListOf(options[selectedItemPosition])))
                // set text on textView and set ellipses so that it does not exceed the box
                displayTextOnTextView(textView, options[selectedItemPosition])
            }
        }

        setClearAllButton(builder, textView, title)
        builder.show()
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
                getString(R.string.card_name) -> {
                    name = valuesMap[i]!![0]
                }

                getString(R.string.layout_name) -> {
                    for (j in valuesMap[i]!!) {
                        layout.add(MagicLayout.valueOf(j))
                    }
                }
                getString(R.string.cmc) -> {
                    for (j in valuesMap[i]!!) {
                        convertedManaCost.add(j.toInt())
                    }
                }
            }
        }
        return Filter(name, layout, convertedManaCost)
    }

    /**
     * Put the cards in the filtered cards array if they match the filter
     */
    private fun filterCards() {
        filteredCards.clear()
        for (card in cards) {
            if (filter.doesContain(card)) {
                filteredCards.add(card)
            }
        }
    }

    /**
     * Get the cards from the intent
     */
    private fun getCardsFromIntent(): ArrayList<MagicCard> {
        val gson = Gson()
        val cardsJson = intent.getStringExtra("cards")
        val type: Type = object : TypeToken<List<MagicCard?>?>() {}.type
        return gson.fromJson(cardsJson, type)
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
     * Add listener to the text view that needs to show multi choice dialog
     */
    private fun addListenerToMultiChoice(textView: TextView, title: String, itemsArray: Array<String>) {
        textView.setOnClickListener {
            val selectedItems = BooleanArray(itemsArray.size)
            for (i in itemsArray) {
                if (valuesMap.containsKey(title) && valuesMap[title]!!.contains(i)) {
                    selectedItems[itemsArray.indexOf(i)] = true
                }
            }
            showMultiChoiceDialog(textView, title, selectedItems,
                itemsArray)
        }
    }

    /**
     * Add listener to the text view that needs to show single choice dialog
     */
    private fun addListenerToSingleChoice(textView: TextView, title: String, itemsArray: Array<String>) {
        textView.setOnClickListener {
            var selectedItem = -1
            for (i in itemsArray.indices) {
                if (valuesMap.containsKey(title) && valuesMap[title]!!.contains(itemsArray[i])) {
                    selectedItem = i
                }
            }
            showSingleChoiceDialog(textView, title, selectedItem, itemsArray)
        }
    }

    /**
     * Display the cards that have been filter in an alertDialog
     * TODO change this method, it is here since the card display has not been implemented
     */
    private fun displayCardsFiltered(button: Button) {
        val builder = AlertDialog.Builder(this@FilterCardsActivity)
        builder.setMessage(filteredCards.toString())
        builder.setCancelable(false)
        builder.setTitle("Cards have been filtered")
        builder.setPositiveButton("OK") { _, _ -> button.isEnabled = true}
        builder.show()
    }
}
