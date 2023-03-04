package com.github.bjolidon.bootcamp

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.util.*


class FilterCardsActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_filter_cards)
//        title = "Filter Cards"
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//
//        val languageSpinner: Spinner = findViewById(R.id.filter_language_spinner)
//        val items = arrayOf("English", "Spanish", "French", "German", "Italian", "Portuguese", "Russian", "Japanese", "Chinese")
//        languageSpinner.setAdapter(ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, items))
//
//        val nameSpinner: Spinner = findViewById(R.id.filter_name_spinner)
//        val items2 = arrayOf("Mur de gel", "Pourrissement cérébral")
//        nameSpinner.setAdapter(ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, items2))
//    }

    lateinit var selectedLanguage: BooleanArray
//    var langList: ArrayList<Int> = ArrayList()
    var languageArray = arrayOf("Java", "C++", "Kotlin", "C", "Python", "Javascript")

    var valuesMap: Map<String, ArrayList<String>> = emptyMap<String, ArrayList<String>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter_cards)

        // assign variable
        val languageTextView: TextView = findViewById(R.id.textView)

        // initialize selected language array
        languageTextView.setOnClickListener {
            // show multi choice dialog
            val selectedLanguage = BooleanArray(languageArray.size)
            for (i in languageArray) {
                if (valuesMap.containsKey("Language") && valuesMap["Language"]!!.contains(i)) {
                    selectedLanguage[languageArray.indexOf(i)] = true
                }
            }
            showMultiChoiceDialog(languageTextView, "Language", selectedLanguage,
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