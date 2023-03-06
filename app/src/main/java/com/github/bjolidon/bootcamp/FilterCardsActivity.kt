package com.github.bjolidon.bootcamp

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.AdapterView.INVALID_POSITION
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.github.bjolidon.bootcamp.model.Filter
import com.github.bjolidon.bootcamp.model.MagicCard
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

    private val languageArray = arrayOf("Java", "C++", "Kotlin", "C", "Python", "Javascript")
    private val namesArray = arrayOf("Meandering Towershell", "Angel of Mercy", "Angel of Serenity", "Angel of Sanctions", "Angel of the Dire Hour")

    private var valuesMap: Map<String, ArrayList<String>> = emptyMap<String, ArrayList<String>>()
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

        val languageTextView: TextView = findViewById(R.id.languageTextView)
        languageTextView.setOnClickListener {
            val selectedLanguage = BooleanArray(languageArray.size)
            for (i in languageArray) {
                if (valuesMap.containsKey(getString(R.string.language)) && valuesMap[getString(R.string.language)]!!.contains(i)) {
                    selectedLanguage[languageArray.indexOf(i)] = true
                }
            }
            showMultiChoiceDialog(languageTextView, getString(R.string.language),
                selectedLanguage, languageArray)
        }

        val nameTextView: TextView = findViewById(R.id.cardNameTextView)
        nameTextView.setOnClickListener {
            var selectedName = -1
            for (i in namesArray.indices) {
                if (valuesMap.containsKey(getString(R.string.card_name))
                    && valuesMap[getString(R.string.card_name)]!!.contains(namesArray[i])) {
                    selectedName = i
                }
            }
            showSingleChoiceDialog(nameTextView, getString(R.string.card_name), selectedName,
                namesArray)
        }
    }
    /*
     * Method to show multi choice dialog
     * found on https://www.geeksforgeeks.org/how-to-implement-multiselect-dropdown-in-android/
     */

    private fun showMultiChoiceDialog(textView: TextView, title: String,
                                      selectedLanguage: BooleanArray, itemsArray: Array<String>) {

        val filtersList: ArrayList<Int> = ArrayList()
        // add already selected items in the filtersList
        for (index in selectedLanguage.indices) {
            if (selectedLanguage[index]) {
                filtersList.add(index)
            }
        }
        val builder: AlertDialog.Builder =
            AlertDialog.Builder(this@FilterCardsActivity)
        builder.setTitle(title)
        builder.setCancelable(false)

        builder.setMultiChoiceItems(itemsArray, selectedLanguage
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

            valuesMap = valuesMap.plus(Pair(title, selectedItems))

            // set text on textView and set ellipses so that it does not exceed the box
            textView.maxWidth = textView.measuredWidth
            textView.maxLines = 1
            textView.setHorizontallyScrolling(true)
            textView.movementMethod = ScrollingMovementMethod()
            textView.text = stringBuilder.toString()
        }

        builder.setNeutralButton("Clear All"
        ) { _, _ ->
            filtersList.clear()
            textView.text = title
            valuesMap = valuesMap.minus(title)
        }
        builder.show()
    }

    /*
     * Method to show single choice dialog
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
            }

            // set text on textView and set ellipses so that it does not exceed the box
            textView.maxWidth = textView.measuredWidth
            textView.maxLines = 1
            textView.setHorizontallyScrolling(true)
            textView.movementMethod = ScrollingMovementMethod()
            textView.text = options[selectedItemPosition]
        }

        builder.setNeutralButton("Clear All"
        ) { _, _ ->
            textView.text = title
            valuesMap = valuesMap.minus(title)
        }
        builder.show()
    }

    private fun convertToFilter() {
        for (i in valuesMap.keys) {
            when (i) {
                getString(R.string.card_name) -> {
                    filter = Filter(valuesMap[i]!![0])
                }
            }
        }

    }

    /*
     * Method to get the cards from the intent
     */
    private fun getCardsFromIntent(): ArrayList<MagicCard> {
        val gson = Gson();
        val cardsJson = intent.getStringExtra("cards")
        val type: Type = object : TypeToken<List<MagicCard?>?>() {}.type
        return gson.fromJson(cardsJson, type)
    }

}
