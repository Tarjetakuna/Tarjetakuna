package com.github.bjolidon.bootcamp

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.util.*


class FilterCardsActivity : AppCompatActivity() {

    var languageArray = arrayOf("Java", "C++", "Kotlin", "C", "Python", "Javascript")
    var valuesMap: Map<String, ArrayList<String>> = emptyMap<String, ArrayList<String>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter_cards)
        title = "Filter Cards"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val languageTextView: TextView = findViewById(R.id.textView)
        languageTextView.setOnClickListener {
            val selectedLanguage = BooleanArray(languageArray.size)
            for (i in languageArray) {
                if (valuesMap.containsKey(getString(R.string.language)) && valuesMap[getString(R.string.language)]!!.contains(i)) {
                    selectedLanguage[languageArray.indexOf(i)] = true
                }
            }
            showMultiChoiceDialog(languageTextView, getString(R.string.language), selectedLanguage,
                languageArray)
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

}