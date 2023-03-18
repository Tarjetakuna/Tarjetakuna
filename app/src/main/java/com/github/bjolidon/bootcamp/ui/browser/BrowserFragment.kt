package com.github.bjolidon.bootcamp.ui.browser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.bjolidon.bootcamp.R
import com.github.bjolidon.bootcamp.databinding.FragmentBrowserBinding
import com.github.bjolidon.bootcamp.ui.singlecard.SingleCardFragment

class BrowserFragment : Fragment() {

    private var _binding: FragmentBrowserBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val browserViewModel = ViewModelProvider(this)[BrowserViewModel::class.java]

        _binding = FragmentBrowserBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textBrowser
        browserViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        binding.listOfCardsRecyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = DisplayCardsAdapter(browserViewModel.cards)
        binding.listOfCardsRecyclerView.adapter = adapter

        adapter.onCardClickListener = object : DisplayCardsAdapter.OnCardClickListener {
            override fun onCardClick(position: Int) {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment_content_drawer, SingleCardFragment(browserViewModel.cards[position]))
                    .addToBackStack(null)
                    .setReorderingAllowed(true)
                    .commit()
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
