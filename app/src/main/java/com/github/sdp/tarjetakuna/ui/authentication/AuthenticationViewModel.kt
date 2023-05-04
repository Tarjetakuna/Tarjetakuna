package com.github.sdp.tarjetakuna.ui.authentication

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.github.sdp.tarjetakuna.utils.Utils


/**
 * View model of the authentication fragment. It does the computations and stores the data for the UI
 * to display.
 */
class AuthenticationViewModel : ViewModel() {

    private val _reportErrorString = MutableLiveData<String>()
    val reportErrorString: LiveData<String> = _reportErrorString

    private val _signInIntent = MutableLiveData<Intent>()
    val signInIntent: LiveData<Intent> = _signInIntent

    private val _changeToSignedInHomeFragment = MutableLiveData<Boolean>()
    val changeToSignedInHomeFragment: LiveData<Boolean> = _changeToSignedInHomeFragment

    private val _changeToAuthenticationButtonFragment = MutableLiveData<Boolean>()
    val changeToAuthenticationButtonFragment: LiveData<Boolean> =
        _changeToAuthenticationButtonFragment

    /**
     * Create an intent to sign in the user and wait for the result.
     */
    fun createSignInIntent(baseContext: Context) {
        // Choose authentication providers
        val providers = arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build()
        )
        // Check if the device is connected to the internet
        if (Utils.isNetworkAvailable(baseContext)) {
            // Create and launch sign-in intent
            val signInIntent2 = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build()
            _signInIntent.value = signInIntent2
        } else {
            _reportErrorString.value = "networkNotAvailable"
        }
    }

    /**
     * Check if the authentication was successful and start the signed in Home fragment if it was.
     * Otherwise, return to the AuthenticationButtonActivity with the appropriate error.
     */
    fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            // Successfully signed in
            //val user = FirebaseAuth.getInstance().currentUser
            // The observer will be notified and launch the SignOutFragment
            _changeToSignedInHomeFragment.value = true
        } else {
            if (response == null) {
                // The observer will be notified and launch the authenticationButtonFragment
                _changeToAuthenticationButtonFragment.value = true
            } else {
                // The observer will be notified and launch the error
                _reportErrorString.value = "authenticationFailed"
            }
        }
    }


}
