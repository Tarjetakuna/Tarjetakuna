package com.github.sdp.tarjetakuna.ui.authentication

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.sdp.tarjetakuna.R

class AuuthenticationFragment : Fragment() {

    companion object {
        fun newInstance() = AuuthenticationFragment()
    }

    private lateinit var viewModel: AuuthenticationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_auuthentication, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AuuthenticationViewModel::class.java)
        // TODO: Use the ViewModel
    }

}