package com.github.sdp.tarjetakuna.database

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.sdp.tarjetakuna.R

class UserCardsFragment : Fragment() {

    companion object {
        fun newInstance() = UserCardsFragment()
    }

    private lateinit var viewModel: UserCardsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_cards, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(UserCardsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}