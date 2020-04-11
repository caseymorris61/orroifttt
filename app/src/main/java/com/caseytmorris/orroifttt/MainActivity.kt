package com.caseytmorris.orroifttt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        turn_on_button.setOnClickListener {
            Log.i("TurnOnClick",  "IFTTT On Clicked")
            sendIFTTTRequest(getString(R.string.turn_on_webhook))
        }

        turn_off_button.setOnClickListener {
            Log.i("TurnOffClick","IFTTT Off Clicked")
            sendIFTTTRequest(getString(R.string.turn_off_webhook))
        }
    }

    private fun sendIFTTTRequest(event:String){
        val queue = Volley.newRequestQueue(this)
        val url = "https://maker.ifttt.com/trigger/$event/with/key/${getString(R.string.webhooks_key)}"

        // Request a string response from the provided URL.
        val stringRequest = StringRequest(
            Request.Method.POST, url,
            Response.Listener<String> { response ->
                // Display the first 500 characters of the response string.
                Toast.makeText(applicationContext,"Success! Lighting action in progress!",
                    Toast.LENGTH_SHORT).show()
            },
            Response.ErrorListener {
                Toast.makeText(applicationContext,"Error Processing Lighting Request!",
                    Toast.LENGTH_SHORT).show()
            }
        )

        Log.i("sendIFTTTRequest",  "IFTTT URL: $url")

        // Add the request to the RequestQueue.
        queue.add(stringRequest)
    }
}
