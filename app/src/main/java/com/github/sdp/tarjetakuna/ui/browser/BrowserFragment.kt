package com.github.sdp.tarjetakuna.ui.browser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.sdp.tarjetakuna.MainActivity
import com.github.sdp.tarjetakuna.R
import com.github.sdp.tarjetakuna.databinding.FragmentBrowserBinding
import com.google.gson.Gson

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
                val bundle = Bundle()
                bundle.putString("card", Gson().toJson(browserViewModel.cards[position]))
                (requireActivity() as MainActivity).changeFragment(R.id.nav_single_card, bundle)
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
