package com.github.bjolidon.bootcamp.ui.authentication

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.AuthUI.IdpConfig
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.github.bjolidon.bootcamp.R
import com.google.firebase.auth.FirebaseAuth

/**
 * This activity is used to sign in or sign out the user.
 * Called by creating an Intent with the extra "signIn" set to true if the user wants to sign in
 * or false if the user wants to sign out.
 */
class AuthenticationActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)
        val signIn = intent.getBooleanExtra("signIn", false)
        if (signIn) {
            createSignInIntent()
        } else {
            signOut()
        }
    }

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }

    /**
     * Create an intent to sign in the user and wait for the result.
     */
    private fun createSignInIntent() {
        // Choose authentication providers
        val providers = arrayListOf(
            IdpConfig.GoogleBuilder().build()
        )
        // Check if the device is connected to the internet
        if(isNetworkAvailable(this.baseContext)) {
            // Create and launch sign-in intent
            val signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build()
            signInLauncher.launch(signInIntent)
        } else {
            reportError("networkNotAvailable")
        }
    }

    /**
     * Check if the authentication was successful and start the SignOutActivity if it was.
     * Otherwise, return to the AuthenticationButtonActivity with the appropriate error.
     */
    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            // Successfully signed in
            val user = FirebaseAuth.getInstance().currentUser
            val intent = Intent(this, SignOutActivity::class.java)
            startActivity(intent)
        } else {
            if (response == null) {
                val intent = Intent(this, AuthenticationButtonActivity::class.java)
                startActivity(intent)
            } else {
                reportError("authenticationFailed")
            }
        }
    }

    /**
     * Sign out the user and return to the AuthenticationButtonActivity.
     */
    private fun signOut() {
        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener {
                val intent = Intent(this, AuthenticationButtonActivity::class.java)
                startActivity(intent)
            }
    }

    /**
     * Check if the device is connected to the internet
     */
    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val nw = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            //for other device that are able to connect with Ethernet
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            //for check internet over Bluetooth
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
            else -> false
        }
    }

    /**
     * Return to the AuthenticationButtonActivity with the appropriate error code
     */
    private fun reportError(errorCode: String) {
        val intent = Intent(this, AuthenticationButtonActivity::class.java)
        intent.putExtra("errorCode", errorCode)
        startActivity(intent)
    }
}
