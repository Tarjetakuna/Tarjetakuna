package com.github.bjolidon.bootcamp.ui.singlecard

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.bjolidon.bootcamp.databinding.FragmentSingleCardBinding
import com.github.bjolidon.bootcamp.model.MagicCard
import com.github.bjolidon.bootcamp.utils.Utils
import java.util.concurrent.Executors

class SingleCardFragment(card: MagicCard) : Fragment() {

    private var _binding: FragmentSingleCardBinding? = null
    private val binding get() = _binding!!

    private var card: MagicCard

    init {
        this.card = card
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSingleCardBinding.inflate(inflater, container, false)

        binding.singleCardNameCard.text = card.name

        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())

        var image: Bitmap? = null
        executor.execute {
            try {
                val inputStream = java.net.URL(card.imageUrl).openStream()
                image = BitmapFactory.decodeStream(inputStream)
                handler.post {
                    binding.singleCardImage.setImageBitmap(image)
                }
            }
            catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
