package com.github.sdp.tarjetakuna.ui.authentication

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.sdp.tarjetakuna.MainActivity
import com.github.sdp.tarjetakuna.R
import com.github.sdp.tarjetakuna.databinding.FragmentSignOutBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


/**
 * This activity is used to display a button to sign out the user.
 */
class SignOutFragment : Fragment() {


    private lateinit var viewModel: SignOutViewModel

    private var _binding: FragmentSignOutBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[SignOutViewModel::class.java]
        _binding = FragmentSignOutBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val user = Firebase.auth.currentUser
        val newMessage = user?.displayName
        val greetingMessage: TextView = binding.greetingMessage
        greetingMessage.text = "Hello $newMessage!"

        val signOutButton = binding.signOutButton
        signOutButton.setOnClickListener {
            val mainActivity = requireActivity() as MainActivity
            val bundle = Bundle()
            bundle.putBoolean("signIn", false)
            mainActivity.changeFragment(R.id.nav_authentication, bundle)
        }
        val backButton = binding.buttonBackHome
        backButton.setOnClickListener {
            val mainActivity = requireActivity() as MainActivity
            mainActivity.changeFragment(R.id.nav_home)
        }

        val configureCollectionButton = binding.manageCollectionButton
        configureCollectionButton.setOnClickListener {
            val mainActivity = requireActivity() as MainActivity
            mainActivity.changeFragment(R.id.nav_user_cards)
        }

        return root
    }

}
