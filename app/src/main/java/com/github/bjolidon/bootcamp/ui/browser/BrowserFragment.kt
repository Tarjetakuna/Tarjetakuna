package com.github.bjolidon.bootcamp.ui.browser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.bjolidon.bootcamp.databinding.FragmentBrowserBinding

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
    val browserViewModel =
            ViewModelProvider(this).get(BrowserViewModel::class.java)

    _binding = FragmentBrowserBinding.inflate(inflater, container, false)
    val root: View = binding.root

    val textView: TextView = binding.textBrowser
    browserViewModel.text.observe(viewLifecycleOwner) {
      textView.text = it
    }
    return root
  }

override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}