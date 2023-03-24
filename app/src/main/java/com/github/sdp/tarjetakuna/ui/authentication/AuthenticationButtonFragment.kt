package com.github.sdp.tarjetakuna.ui.authentication

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.github.sdp.tarjetakuna.MainActivity
import com.github.sdp.tarjetakuna.R
import com.github.sdp.tarjetakuna.databinding.FragmentAuthenticationBinding
import com.github.sdp.tarjetakuna.databinding.FragmentAuthenticationButtonBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

/**
 * This fragment is used to display a button to sign in the user and report an error if the
 * authentication failed or if the user is not connected to the internet.
 */
class AuthenticationButtonFragment : Fragment() {

    private lateinit var viewModel: AuthenticationButtonViewModel

    private var _binding: FragmentAuthenticationButtonBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[AuthenticationButtonViewModel::class.java]
        _binding = FragmentAuthenticationButtonBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val user = Firebase.auth.currentUser

        if (user != null) {
            val mainActivity = requireActivity() as MainActivity
            mainActivity.changeFragment(R.id.nav_sign_out)
        } else {
            setupErrorMsg()

            val button = binding.connectionButton
            button.setOnClickListener {
                val mainActivity = requireActivity() as MainActivity
                val bundle = Bundle()
                bundle.putBoolean("signIn", true)
                mainActivity.changeFragment(R.id.nav_authentication, bundle)
            }

            val backButton = binding.buttonBackHome
            backButton.setOnClickListener {
                val mainActivity = requireActivity() as MainActivity
                mainActivity.changeFragment(R.id.nav_home)
            }
        }
        return root
    }


    /**
     * This function is used to display an error message if the user is not connected to the internet
     * or if the authentication failed.
     */
    private fun setupErrorMsg() {
        val errorCode = arguments?.getString("errorCode")
        val connexionErrorMsg = binding.connexionErrorMsg
        val authFailedErrorMsg = binding.authFailedErrorMsg
        connexionErrorMsg.visibility = View.INVISIBLE
        authFailedErrorMsg.visibility = View.INVISIBLE
        if (errorCode == "authFailed") {
            authFailedErrorMsg.visibility = View.VISIBLE
        } else if (errorCode == "networkNotAvailable") {
            connexionErrorMsg.visibility = View.VISIBLE
        }
    }

}
