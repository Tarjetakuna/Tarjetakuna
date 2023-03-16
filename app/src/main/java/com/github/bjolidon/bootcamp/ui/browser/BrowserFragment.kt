package com.github.bjolidon.bootcamp.ui.browser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.bjolidon.bootcamp.databinding.FragmentBrowserBinding
import com.github.bjolidon.bootcamp.model.MagicCard
import com.github.bjolidon.bootcamp.model.MagicLayout
import com.github.bjolidon.bootcamp.model.MagicSet

class BrowserFragment : Fragment() {

    private var _binding: FragmentBrowserBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val cards: ArrayList<MagicCard> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val browserViewModel = ViewModelProvider(this)[BrowserViewModel::class.java]

        _binding = FragmentBrowserBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // simulate the cards
        generateCards()

        val textView: TextView = binding.textBrowser
        browserViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        setUpRecyclerView()
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpRecyclerView() {
        val recyclerView = binding.listOfCardsRecycleView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = DisplayCardsAdapter(cards)
    }

    /**
     * Generate cards in order to simulate the display of the cards
     */
    private fun generateCards() {
        for (i in 0..39) {
            val name = if (i+1 < 10) { "Magic 190${i+1}" } else { "Magic 19${i+1}" }
            val card = MagicCard(
                "Angel of Mercy ${i+1}", "Flying",
                MagicLayout.Normal, 7, "{5}{W}{W}",
                MagicSet("MT15", name), 56,
                "https://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=149935&type=card"
            )
            cards.add(card)
        }
    }
}