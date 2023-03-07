package com.github.bjolidon.bootcamp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button: Button = findViewById(R.id.mainGoButton)
        val plainText: EditText = findViewById(R.id.mainName)

        button.setOnClickListener{
//            val intent = Intent(this, GreetingActivity::class.java)
//            intent.putExtra("name2", plainText.text.toString())
            val intent2 = Intent(this, FilterCardsActivity::class.java)
            intent2.putExtra("cards", "[]")
            startActivity(intent2)
//            startActivity(intent)
        }

    }

}