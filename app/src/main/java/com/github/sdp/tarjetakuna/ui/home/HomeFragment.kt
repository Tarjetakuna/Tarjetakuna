package com.github.sdp.tarjetakuna.ui.home

import android.app.AlertDialog
import android.content.DialogInterface
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
import com.github.sdp.tarjetakuna.database.local.LocalDatabaseProvider
import com.github.sdp.tarjetakuna.databinding.FragmentHomeBinding
import com.github.sdp.tarjetakuna.ui.authentication.SignIn

/**
 * Fragment for the home page.
 */
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
        val mainActivity = requireActivity() as MainActivity
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val arrayCards = ArrayList<String>()
        arrayCards.add("cards")
        LocalDatabaseProvider.deleteDatabases(requireContext(), arrayCards)

        //Check if user is signed in and display the correct home page
        homeViewModel.checkUserConnected()
        homeViewModel.isConnected.observe(viewLifecycleOwner) {
            displayUserHome(it)
        }

        //Welcome text on home page
        val textView: TextView = binding.homeWelcomeText
        homeViewModel.titleText.observe(viewLifecycleOwner) {
            textView.text = it

        }
        //check if sign in error has occurred
        setupErrorMsg()

        val descTextView: TextView = binding.homeWelcomeDescription
        homeViewModel.descriptionText.observe(viewLifecycleOwner) {
            descTextView.text = it
        }

        val greetingMessage: TextView = binding.homeUserGreetingText
        greetingMessage.text =
            getString(R.string.home_welcome_signed_in, SignIn.getSignIn().getUserDisplayName())

        //Local database
        // TODO remove when not owned card search is implemented
        homeViewModel.localDatabase = LocalDatabaseProvider.setDatabase(
            requireContext(),
            LocalDatabaseProvider.CARDS_DATABASE_NAME
        )

        //Buttons
        val authenticationButton: Button = binding.homeAuthenticationButton
        authenticationButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putBoolean("signIn", true)
            mainActivity.changeFragment(R.id.nav_authentication, bundle)
        }

        val addRandomCardButton: Button = binding.addRandomCardButton
        addRandomCardButton.setOnClickListener {
            homeViewModel.addRandomCard()
        }

        val positiveButtonClick = { dialog: DialogInterface, which: Int ->
            val bundle = Bundle()
            bundle.putBoolean("signIn", false)
            mainActivity.changeFragment(R.id.nav_authentication, bundle)
        }

        val signOutButton: Button = binding.homeSignOutButton
        signOutButton.setOnClickListener {
            val confirmSignOut = AlertDialog.Builder(requireContext(), R.style.AlertDialogCustom)
            with(confirmSignOut) {
                setTitle(R.string.confirmSignOut_question)
                setPositiveButton(
                    R.string.confirmSignOut_yes,
                    DialogInterface.OnClickListener(function = positiveButtonClick)
                )
                setNegativeButton(R.string.confirmSignOut_no, null)
                show()
            }
        }
        return root
    }

    /**
     * Display different information, based on whether the user is signed in or not.
     */
    private fun displayUserHome(userIsConnected: Boolean) {
        if (userIsConnected) {
            binding.homeWelcomeText.visibility = View.GONE
            binding.homeWelcomeDescription.visibility = View.GONE
            binding.homeAuthenticationButton.visibility = View.GONE
            binding.homeUserContentLayout.visibility = View.VISIBLE
        } else {
            binding.homeWelcomeText.visibility = View.VISIBLE
            binding.homeWelcomeDescription.visibility = View.VISIBLE
            binding.homeAuthenticationButton.visibility = View.VISIBLE
            binding.homeUserContentLayout.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * This function is used to display an error message if the user is not connected to the internet
     * or if the authentication failed.
     */
    private fun setupErrorMsg() {
        val errorCode = arguments?.getString("errorCode")
        val authFailedErrorMsg = binding.homeAuthenticationFailedText
        authFailedErrorMsg.visibility = View.INVISIBLE
        if (errorCode == "authFailed") {
            authFailedErrorMsg.text = getString(R.string.authentication_failed_please_try_again)
            authFailedErrorMsg.visibility = View.VISIBLE
        } else if (errorCode == "networkNotAvailable") {
            authFailedErrorMsg.text = getString(R.string.device_is_not_connected_to_internet)
            authFailedErrorMsg.visibility = View.VISIBLE
        }

    }
}
