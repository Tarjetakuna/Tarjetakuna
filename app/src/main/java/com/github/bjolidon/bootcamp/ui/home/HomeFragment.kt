package com.github.bjolidon.bootcamp.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.bjolidon.bootcamp.ui.filter.FilterCardsActivity
import com.github.bjolidon.bootcamp.databinding.FragmentHomeBinding
import com.github.bjolidon.bootcamp.model.MagicCard
import com.github.bjolidon.bootcamp.model.MagicLayout
import com.github.bjolidon.bootcamp.model.MagicSet
import com.google.gson.Gson

class HomeFragment : Fragment() {

  val cards: ArrayList<MagicCard> = arrayListOf(
    MagicCard("Meandering Towershell", "Islandwalk",
      MagicLayout.Normal, 5, "{3}{G}{G}",
      MagicSet("MT15", "Magic 2015"), 141,
      "https://gatherer.wizards.com/Pages/Card/Details.aspx?multiverseid=386602"),
    MagicCard("Angel of Mercy", "Flying",
      MagicLayout.Normal, 5, "{4}{W}",
      MagicSet("MT15", "Magic 2015"), 1,
      "https://gatherer.wizards.com/Pages/Card/Details.aspx?multiverseid=82992")
  )

private var _binding: FragmentHomeBinding? = null
  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

    _binding = FragmentHomeBinding.inflate(inflater, container, false)
    val root: View = binding.root

    val textView: TextView = binding.textHome
    homeViewModel.text.observe(viewLifecycleOwner) {
      textView.text = it

    }

    val plainText: EditText = binding.mainName2
    val button: Button = binding.mainGoButton2
    button.setOnClickListener {
    }

    val filterActivityButton: Button = binding.filterActivityButton
    filterActivityButton.setOnClickListener {
      val intent = Intent(activity, FilterCardsActivity::class.java)
      val gson = Gson()
      intent.putExtra("cards", gson.toJson(cards))
      startActivity(intent)
    }



    return root
  }

override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}