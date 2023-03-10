package com.github.bjolidon.bootcamp.ui.authentication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.bjolidon.bootcamp.MainActivity
import com.github.bjolidon.bootcamp.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

/**
 * This activity is used to display a button to sign in the user and report an error if the
 * authentication failed or if the user is not connected to the internet.
 */
class AuthenticationButtonActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val user = Firebase.auth.currentUser

        if (user != null) {
            val intent = Intent(this, SignOutActivity::class.java)
            startActivity(intent)
        } else {
            setContentView(R.layout.activity_authentication_button)

            setupErrorMsg()

            val button = findViewById<Button>(R.id.connectionButton)
            button.setOnClickListener {
                val intent = Intent(this, AuthenticationActivity::class.java)
                intent.putExtra("signIn", true)
                startActivity(intent)
            }

            val backButton = findViewById<Button>(R.id.button_back_home)
            backButton.setOnClickListener {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }

    /**
     * This function is used to display an error message if the user is not connected to the internet
     * or if the authentication failed.
     */
    private fun setupErrorMsg() {
        val errorCode = intent.getStringExtra("errorCode")
        val connexionErrorMsg = findViewById<TextView>(R.id.connexionErrorMsg)
        val authFailedErrorMsg = findViewById<TextView>(R.id.authFailedErrorMsg)
        connexionErrorMsg.visibility = View.INVISIBLE
        authFailedErrorMsg.visibility = View.INVISIBLE
        if (errorCode == "authFailed") {
            authFailedErrorMsg.visibility = View.VISIBLE
        } else if (errorCode == "networkNotAvailable") {
            connexionErrorMsg.visibility = View.VISIBLE
        }
    }
}
