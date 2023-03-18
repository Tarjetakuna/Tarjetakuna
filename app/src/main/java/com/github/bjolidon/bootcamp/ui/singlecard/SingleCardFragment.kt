package com.github.bjolidon.bootcamp.ui.singlecard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.bjolidon.bootcamp.R
import com.github.bjolidon.bootcamp.model.MagicCard

class SingleCardFragment(card: MagicCard) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_single_card, container, false)
    }
}
