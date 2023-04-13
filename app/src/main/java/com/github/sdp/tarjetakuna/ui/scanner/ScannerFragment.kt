package com.github.sdp.tarjetakuna.ui.scanner

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.sdp.tarjetakuna.databinding.FragmentScannerBinding

/**
 * This fragment is responsible to take a picture of the card
 */
class ScannerFragment : Fragment() {
    private var _binding: FragmentScannerBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val scannerViewModel = ViewModelProvider(this)[ScannerViewModel::class.java]

        _binding = FragmentScannerBinding.inflate(inflater, container, false)
        val root: View = binding.root

        scannerViewModel.textInformationId.observe(viewLifecycleOwner) {
            binding.textInformation.text = getString(it)
        }

        scannerViewModel.picture.observe(viewLifecycleOwner) {
            binding.imageCard.setImageBitmap(it)
            binding.btAnalyse.isEnabled = true
        }

        scannerViewModel.textDetected.observe(viewLifecycleOwner) {
            binding.textInImage.text = it.text
        }

        val registerImage =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                scannerViewModel.setActivityResult(it)
            }

        binding.buttonScan.setOnClickListener {
            registerImage.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
        }

        binding.btAnalyse.setOnClickListener {
            scannerViewModel.detectText()
            scannerViewModel.detectObject()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
