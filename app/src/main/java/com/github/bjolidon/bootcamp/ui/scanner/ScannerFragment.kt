package com.github.bjolidon.bootcamp.ui.scanner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.bjolidon.bootcamp.databinding.FragmentScannerBinding

class ScannerFragment : Fragment() {

private var _binding: FragmentScannerBinding? = null
  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    val scannerViewModel =
            ViewModelProvider(this).get(ScannerViewModel::class.java)

    _binding = FragmentScannerBinding.inflate(inflater, container, false)
    val root: View = binding.root

    val textView: TextView = binding.textScanner
    scannerViewModel.text.observe(viewLifecycleOwner) {
      textView.text = it
    }
    return root
  }

override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}