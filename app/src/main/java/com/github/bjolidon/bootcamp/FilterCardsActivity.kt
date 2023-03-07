package com.github.bjolidon.bootcamp

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.AdapterView.INVALID_POSITION
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.github.bjolidon.bootcamp.model.Filter
import com.github.bjolidon.bootcamp.model.MagicCard
import com.github.bjolidon.bootcamp.model.MagicLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import kotlin.collections.ArrayList


/*
    * Activity to filter cards.
    * Get the list of cards to be filtered from the intent as a string gson
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
    lateinit var filteredCards: ArrayList<MagicCard>
    lateinit var filter: Filter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter_cards)
        title = "Filter Cards"
        // take the cards from the intent so that we can process them
        cards = getCardsFromIntent()
        filteredCards = ArrayList()


        val manaCostTextView: TextView = findViewById(R.id.manaCostTextView)
        addListenerToMultiChoice(manaCostTextView, getString(R.string.card_mana_cost), manaCostArray)

        val layoutTextView: TextView = findViewById(R.id.layoutTextView)
        addListenerToMultiChoice(layoutTextView, getString(R.string.layout_name), layoutArray)

        val nameTextView: TextView = findViewById(R.id.cardNameTextView)
        addListenerToSingleChoice(nameTextView, getString(R.string.card_name), namesArray)


        val applyFilterButton: Button = findViewById(R.id.filterButton)
        applyFilterButton.setOnClickListener {
            filter = convertToFilter()
            Toast.makeText(this, "Filter applied", Toast.LENGTH_SHORT).show()
        }
    }
    /*
     * Show multi choice dialog
     * inspired from https://www.geeksforgeeks.org/how-to-implement-multiselect-dropdown-in-android/
     */

    private fun showMultiChoiceDialog(textView: TextView, title: String,
                                      selectedItems: BooleanArray, itemsArray: Array<String>) {

        val filtersList: ArrayList<Int> = ArrayList()
        // add already selected items in the filtersList
        for (index in selectedItems.indices) {
            if (selectedItems[index]) {
                filtersList.add(index)
            }
        }
        val builder: AlertDialog.Builder = AlertDialog.Builder(this@FilterCardsActivity)
        builder.setTitle(title)
        builder.setCancelable(false)

        builder.setMultiChoiceItems(itemsArray, selectedItems
        ) { _, index, boolean ->
            if (boolean) {
                filtersList.add(index)
                filtersList.sort()
            } else {
                filtersList.remove(Integer.valueOf(index))
            }
        }
        builder.setPositiveButton("OK"
        ) { _, _ ->
            // create list to add them in valuesMap
            val selectedItems: ArrayList<String> = ArrayList()
            val stringBuilder = StringBuilder()

            for (index in 0 until filtersList.size) {
                stringBuilder.append(itemsArray[filtersList[index]])
                selectedItems.add(itemsArray[filtersList[index]])
                if (index != (filtersList.size - 1)) {
                    stringBuilder.append(", ")
                }
            }
            if (selectedItems.isNotEmpty()) {
                valuesMap = valuesMap.plus(Pair(title, selectedItems))
            }

            // set text on textView and set ellipses so that it does not exceed the box
            displayTextOnTextView(textView, stringBuilder.toString())
        }

        builder.setNeutralButton("Clear All"
        ) { _, _ ->
            filtersList.clear()
            textView.text = ""
            valuesMap = valuesMap.minus(title)
        }
        builder.show()
    }

    /*
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

        builder.setNeutralButton("Clear All"
        ) { _, _ ->
            textView.text = ""
            valuesMap = valuesMap.minus(title)
        }
        builder.show()
    }

    /*
     * Convert the map of selected values to a filter object
     */
    private fun convertToFilter(): Filter {
        var name = ""
        val layout: ArrayList<MagicLayout> = emptyArray<MagicLayout>().toCollection(ArrayList())
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
            }
        }
        return Filter(name, layout)
    }

    /*
     * Get the cards from the intent
     */
    private fun getCardsFromIntent(): ArrayList<MagicCard> {
        val gson = Gson()
        val cardsJson = intent.getStringExtra("cards")
        val type: Type = object : TypeToken<List<MagicCard?>?>() {}.type
        return gson.fromJson(cardsJson, type)
    }

    /*
        * Set text on textView and set ellipses so that it does not exceed the box
     */
    private fun displayTextOnTextView(textView: TextView, message: String) {
        textView.maxWidth = textView.measuredWidth
        textView.maxLines = 1
        textView.setHorizontallyScrolling(true)
        textView.movementMethod = ScrollingMovementMethod()
        textView.text = message
    }

    /*
     * Add listener to the text view that needs to show multi choice dialog
     */
    private fun addListenerToMultiChoice(textView: TextView, title: String, itemsArray: Array<String>) {
        textView.setOnClickListener {
            val selectedLayout = BooleanArray(itemsArray.size)
            for (i in itemsArray) {
                if (valuesMap.containsKey(title) && valuesMap[title]!!.contains(i)) {
                    selectedLayout[itemsArray.indexOf(i)] = true
                }
            }
            showMultiChoiceDialog(textView, title, selectedLayout,
                itemsArray)
        }
    }

    /*
     * Add listener to the text view that needs to show single choice dialog
     */
    private fun addListenerToSingleChoice(textView: TextView, title: String, itemsArray: Array<String>) {
        textView.setOnClickListener {
            var selectedName = -1
            for (i in itemsArray.indices) {
                if (valuesMap.containsKey(title) && valuesMap[title]!!.contains(itemsArray[i])) {
                    selectedName = i
                }
            }
            showSingleChoiceDialog(textView, title, selectedName, itemsArray)
        }
    }
}

