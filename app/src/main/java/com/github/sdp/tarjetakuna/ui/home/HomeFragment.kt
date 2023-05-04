package com.github.sdp.tarjetakuna.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.sdp.tarjetakuna.MainActivity
import com.github.sdp.tarjetakuna.R
import com.github.sdp.tarjetakuna.databinding.FragmentHomeBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        homeViewModel.checkUserConnected()
        homeViewModel.isConnected.observe(viewLifecycleOwner) {
            displayUserHome(it)
        }

        val textView: TextView = binding.homeWelcomeText
        homeViewModel.titleText.observe(viewLifecycleOwner) {
            textView.text = it
        }
        val descTextView: TextView = binding.homeWelcomeDescriptionText
        homeViewModel.descriptionText.observe(viewLifecycleOwner) {
            descTextView.text = it
        }

        val authenticationButton: Button = binding.homeAuthenticationButton
        authenticationButton.setOnClickListener {
            val mainActivity = requireActivity() as MainActivity
            mainActivity.changeFragment(R.id.nav_authentication_button)
        }

        val user = Firebase.auth.currentUser
        val newMessage = user?.displayName
        val greetingMessage: TextView = binding.homeUserGreetingText
        greetingMessage.text = getString(R.string.home_hello, newMessage)

        val signOutButton = binding.homeSignOutButton
        signOutButton.setOnClickListener {
            val mainActivity = requireActivity() as MainActivity
            val bundle = Bundle()
            bundle.putBoolean("signIn", false)
            mainActivity.changeFragment(R.id.nav_authentication, bundle)
        }

        return root
    }

    private fun displayUserHome(userIsConnected: Boolean) {
        if (userIsConnected) {
            binding.homeWelcomeText.visibility = View.GONE
            binding.homeWelcomeDescriptionText.visibility = View.GONE
            binding.homeAuthenticationButton.visibility = View.GONE
            binding.homeUserContentLayout.visibility = View.VISIBLE
        } else {
            binding.homeWelcomeText.visibility = View.VISIBLE
            binding.homeWelcomeDescriptionText.visibility = View.VISIBLE
            binding.homeAuthenticationButton.visibility = View.VISIBLE
            binding.homeUserContentLayout.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
