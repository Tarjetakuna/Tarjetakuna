package com.github.bjolidon.bootcamp.ui.authentication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.bjolidon.bootcamp.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AuthenticationButtonActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val user = Firebase.auth.currentUser

        if (user != null) {
            val intent = Intent(this, SignOutActivity::class.java)
            startActivity(intent)
        } else {
            setContentView(R.layout.activity_authentication_button)

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
            val button = findViewById<Button>(R.id.connectionButton)
            button.setOnClickListener {
                val intent = Intent(this, AuthenticationActivity::class.java)
                intent.putExtra("signIn", true)
                startActivity(intent)
            }
        }
    }
}