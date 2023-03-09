package com.github.bjolidon.bootcamp.ui.scanner

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.bjolidon.bootcamp.databinding.FragmentScannerBinding

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

        scannerViewModel.textInformation.observe(viewLifecycleOwner) {
            binding.textInformation.text = it
        }

        scannerViewModel.image.observe(viewLifecycleOwner) {
            binding.imageCard.setImageBitmap(it)
        }

        val registerImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val image: Bitmap? = it.data?.extras?.getParcelable("data", Bitmap::class.java)
                if (image != null) {
                    scannerViewModel.setImage(image)
                    scannerViewModel.setTextInformation(it.resultCode)
                }
                else {
                    scannerViewModel.setTextInformation(RESULT_CANCELED)
                }
            }
            else {
                scannerViewModel.setTextInformation(it.resultCode)
            }
        }

        binding.buttonScan.setOnClickListener {
          registerImage.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}