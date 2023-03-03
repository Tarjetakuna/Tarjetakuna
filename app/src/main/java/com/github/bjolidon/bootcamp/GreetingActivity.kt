package com.github.bjolidon.bootcamp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class GreetingActivity: AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_greeting)

        val newMessage = intent.getStringExtra("name")
        val greetingMessage: TextView = findViewById(R.id.greetingMessage)
        greetingMessage.text = "Hello $newMessage!"

        val signOutButton = findViewById<TextView>(R.id.signOutButton)
        signOutButton.setOnClickListener {
            val intent = Intent(this, AuthenticationActivity::class.java)
            intent.putExtra("signIn", false)
            startActivity(intent)
        }
    }
}