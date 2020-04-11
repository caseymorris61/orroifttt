package com.caseytmorris.orroifttt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setOnClickListeners()
    }

    fun setOnClickListeners() {
        turn_on_button.setOnClickListener { Log.i("TurnOnClick",  "Casey On Clicked") }

        turn_off_button.setOnClickListener { Log.i("TurnOffClick","Casey OffClicked") }
    }
}
