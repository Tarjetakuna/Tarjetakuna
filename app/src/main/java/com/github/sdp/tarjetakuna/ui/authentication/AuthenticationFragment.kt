package com.github.sdp.tarjetakuna.ui.authentication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.github.sdp.tarjetakuna.MainActivity
import com.github.sdp.tarjetakuna.R
import com.github.sdp.tarjetakuna.database.DatabaseSync
import com.github.sdp.tarjetakuna.database.local.LocalDatabaseProvider
import com.github.sdp.tarjetakuna.databinding.FragmentAuthenticationBinding
import com.github.sdp.tarjetakuna.extra.Location


/**
 * This fragment is used to sign in or sign out the user.
 * Called by creating a bundle with the extra "signIn" set to true if the user wants to sign in
 * or false if the user wants to sign out.
 */
class AuthenticationFragment : Fragment() {

    private lateinit var viewModel: AuthenticationViewModel

    private var _binding: FragmentAuthenticationBinding? = null
    private val binding get() = _binding!!

    // lateinit because we need to initialize viewModel first
    private lateinit var signInLauncher: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[AuthenticationViewModel::class.java]
        _binding = FragmentAuthenticationBinding.inflate(inflater, container, false)
        val root: View = binding.root

        signInLauncher = registerForActivityResult(
            FirebaseAuthUIActivityResultContract()
        ) { res ->
            Location.captureCurrentLocation(requireActivity())
            viewModel.onSignInResult(res)
        }

        viewModel.reportErrorString.observe(viewLifecycleOwner) {
            if (it != null) {
                reportError(it)
            }
        }

        viewModel.signInIntent.observe(viewLifecycleOwner) {
            if (it != null) {
                signInLauncher.launch(it)

            }
        }

        viewModel.signInSuccess.observe(viewLifecycleOwner) {
            if (it) {
                // Sync the database when the user is connected
                LocalDatabaseProvider.deleteDatabases(
                    requireContext(),
                    arrayListOf(LocalDatabaseProvider.CARDS_DATABASE_NAME)
                )
                LocalDatabaseProvider.setDatabase(
                    requireContext(),
                    LocalDatabaseProvider.CARDS_DATABASE_NAME
                )
                DatabaseSync.sync()
                val mainActivity = requireActivity() as MainActivity
                mainActivity.changeFragment(R.id.nav_home)
            }
        }

        viewModel.signInResponseNull.observe(viewLifecycleOwner) {
            if (it) {
                val mainActivity = requireActivity() as MainActivity
                mainActivity.changeFragment(R.id.nav_home)
            }
        }

        val signIn = requireArguments().getBoolean("signIn", false)
        if (signIn) {
            viewModel.createSignInIntent(requireActivity().application.baseContext)
        } else {
            signOut()
        }
        return root
    }

    /**
     * Return to the AuthenticationButtonFragment with the appropriate error code
     */
    private fun reportError(errorCode: String) {
        val mainActivity = requireActivity() as MainActivity
        val bundle = Bundle()
        bundle.putString("errorCode", errorCode)
        mainActivity.changeFragment(R.id.nav_home, bundle)
    }

    /**
     * Sign out the user and return to the HomeFragment.
     */
    private fun signOut() {
        AuthUI.getInstance()
            .signOut(requireContext())
            .addOnCompleteListener {
                val mainActivity = requireActivity() as MainActivity
                mainActivity.changeFragment(R.id.nav_home)
            }
    }
}
