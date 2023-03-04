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
    var langList: ArrayList<Int> = ArrayList()
    var langArray = arrayOf("Java", "C++", "Kotlfsdfsdafsafdsfasdfafdafdsfdsafdsafdsafdsafin", "C", "Python", "Javascript")

    var valuesMap: Map<String, Int> = emptyMap<String, Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter_cards)

        // assign variable
        val textView: TextView = findViewById(R.id.textView)

        // initialize selected language array
        selectedLanguage = BooleanArray(langArray.size)
        textView.setOnClickListener {
            // show multi choice dialog
            showMultiChoiceDialog(textView, "Select Language")
        }
    }
    /*
     * Method to show multi choice dialog
     * found on https://www.geeksforgeeks.org/how-to-implement-multiselect-dropdown-in-android/
     */

    private fun showMultiChoiceDialog(textView: TextView, title: String) {
        // Initialize alert dialog
        val builder: AlertDialog.Builder =
            AlertDialog.Builder(this@FilterCardsActivity)

        // set title
        builder.setTitle(title)

        // set dialog non cancelable
        builder.setCancelable(false)
        builder.setMultiChoiceItems(langArray, selectedLanguage
        ) { _, i, b ->
            // check condition
            if (b) {
                // when checkbox selected
                // Add position  in lang list
                langList.add(i)
                // Sort array list
                langList.sort()
            } else {
                // when checkbox unselected
                // Remove position from langList
                langList.remove(Integer.valueOf(i))
            }
        }
        builder.setPositiveButton("OK"
        ) { _, _ -> // Initialize string builder
            val stringBuilder = StringBuilder()
            // use for loop
            for (j in 0 until langList.size) {
                // concat array value
                stringBuilder.append(langArray[langList[j]])
                // check condition
                if (j != langList.size - 1) {
                    // When j value  not equal
                    // to lang list size - 1
                    // add comma
                    stringBuilder.append(", ")
                }
            }
            // set text on textView and set ellipses so that it does not exceed the box
            textView.maxWidth = textView.measuredWidth
            textView.maxLines = 1
            textView.setHorizontallyScrolling(true)
            textView.movementMethod = ScrollingMovementMethod()
            textView.text = stringBuilder.toString()
        }
        builder.setNegativeButton("Cancel"
        ) { dialogInterface, _ -> // dismiss dialog
            dialogInterface.dismiss()
        }
        builder.setNeutralButton("Clear All"
        ) { _, _ ->
            // use for loop
            for (j in selectedLanguage.indices) {
                // remove all selection
                selectedLanguage[j] = false
                // clear language list
                langList.clear()
                // clear text view value
                textView.text = ""
            }
        }
        // show dialog
        builder.show()
    }

}