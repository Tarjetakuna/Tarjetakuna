package com.github.bjolidon.bootcamp

import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity


class FilterCardsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter_cards)
        title = "Filter Cards"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

}