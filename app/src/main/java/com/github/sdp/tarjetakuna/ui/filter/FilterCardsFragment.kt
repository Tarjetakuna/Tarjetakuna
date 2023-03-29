package com.github.sdp.tarjetakuna.ui.filter

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.sdp.tarjetakuna.MainActivity
import com.github.sdp.tarjetakuna.R
import com.github.sdp.tarjetakuna.databinding.FragmentFilterCardsBinding

/**
 * fragment to filter cards.
 * Get the list of cards to be filtered from the bundle as a json string
 * e.g
 * [{"convertedManaCost": 7,
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
}, ...]
 */
class FilterCardsFragment : Fragment() {

    private var _binding: FragmentFilterCardsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: FilterCardsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[FilterCardsViewModel::class.java]
        _binding = FragmentFilterCardsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel.initializeAttributes(requireArguments())

        viewModel.mediatorLiveData.observe(viewLifecycleOwner) { (textViewId, message) ->
            if (textViewId != null && message != null) {
                displayTextOnTextView(requireView().findViewById(textViewId), message)
            }
        }

        //TODO change the UI for this filter to be either an keyboard entry or a sliding bar
        val manaCostTextView: TextView = binding.cmcTextView
        addListenerToMultiChoice(manaCostTextView, getString(R.string.cmc), viewModel.manaCostArray)

        val layoutTextView: TextView = binding.layoutTextView
        addListenerToMultiChoice(
            layoutTextView,
            getString(R.string.layout_name),
            viewModel.layoutArray
        )

        val nameTextView: TextView = binding.cardNameTextView
        addListenerToSingleChoice(nameTextView, getString(R.string.card_name), viewModel.namesArray)

        val applyFilterButton: Button = binding.filterButton
        applyFilterButton.setOnClickListener {
            // disable button to avoid multiple clicks
            applyFilterButton.isEnabled = false
            viewModel.filterCards()
            displayCardsFiltered(applyFilterButton)
        }

        val returnMainButton: Button = binding.returnMainButton
        returnMainButton.setOnClickListener {
            val mainActivity = requireActivity() as MainActivity
            mainActivity.changeFragment(R.id.nav_home)
        }

        return root
    }


    /**
     * Add listener to the text view that needs to show multi choice dialog
     */
    private fun addListenerToMultiChoice(
        textView: TextView,
        title: String,
        itemsArray: Array<String>
    ) {
        textView.setOnClickListener {
            val selectedItems = viewModel.getAlreadySelectedItems(itemsArray, textView.id)
            showMultiChoiceDialog(
                textView, title, selectedItems,
                itemsArray
            )
        }
    }

    /**
     * Add listener to the text view that needs to show single choice dialog
     */
    private fun addListenerToSingleChoice(
        textView: TextView,
        title: String,
        itemsArray: Array<String>
    ) {
        textView.setOnClickListener {
            val selectedItem = viewModel.getAlreadySelectedItem(itemsArray, textView.id)
            showSingleChoiceDialog(textView, title, selectedItem, itemsArray)
        }
    }

    /**
     * Show multi choice dialog
     * inspired from https://www.geeksforgeeks.org/how-to-implement-multiselect-dropdown-in-android/
     */
    fun showMultiChoiceDialog(
        textView: TextView, title: String,
        selectedItemsBool: BooleanArray, itemsArray: Array<String>
    ) {

        val selectedItemsPositions: ArrayList<Int> = ArrayList()
        // add already selected items in the list of selected items
        for (index in selectedItemsBool.indices) {
            if (selectedItemsBool[index]) {
                selectedItemsPositions.add(index)
            }
        }
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
        builder.setTitle(title)
        builder.setCancelable(false)

        builder.setMultiChoiceItems(
            itemsArray, selectedItemsBool
        ) { _, index, boolean ->
            viewModel.multiChoiceOnChoicesClicked(index, boolean, selectedItemsPositions)
        }

        builder.setPositiveButton(
            "OK"
        ) { _, _ ->
            viewModel.multiChoiceOKButtonClicked(
                textView.id,
                selectedItemsPositions,
                itemsArray
            )
        }

        builder.setNeutralButton(
            "Clear All"
        ) { _, _ ->
            textView.text = ""
            viewModel.setClearAllButton(textView.id)
        }

        builder.show()
    }


    /**
     * Show single choice dialog
     */
    private fun showSingleChoiceDialog(
        textView: TextView, title: String,
        selectedChoice: Int, options: Array<String>
    ) {

        val builder: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
        builder.setTitle(title)
        builder.setCancelable(false)
        builder.setSingleChoiceItems(options, selectedChoice) { _, _ -> }

        builder.setPositiveButton(
            "OK"
        ) { dialog, _ ->
            viewModel.setSingleOKButton(dialog, textView.id, options)
        }

        builder.setNeutralButton(
            "Clear All"
        ) { _, _ ->
            textView.text = ""
            viewModel.setClearAllButton(textView.id)
        }
        builder.show()
    }

    /**
     * Display the cards that have been filter in an alertDialog
     * TODO change this method, it is here since the card display has not been implemented
     */
    private fun displayCardsFiltered(button: Button) {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setMessage(viewModel.getFilterCardsString())
        builder.setCancelable(false)
        builder.setTitle("Cards have been filtered")
        builder.setPositiveButton("OK") { _, _ -> button.isEnabled = true }
        builder.show()
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

}
