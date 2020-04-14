package com.caseytmorris.orroifttt

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley


fun sendIFTTTLightingRequest(event: String, webhookKey: String, context: Context){
    sendIFTTTRequest(event,webhookKey,context) {
        when (it) {
            true -> {
                Toast.makeText(context,"Success! Lighting action in progress!",
                    Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(context,"Error Processing Lighting Request!",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }
}

fun sendIFTTTRequest(event: String,webhookKey: String, context: Context, processRequest: (Boolean) -> Unit) {
    val queue = Volley.newRequestQueue(context)
    val url = "https://maker.ifttt.com/trigger/$event/with/key/$webhookKey"

    // Request a string response from the provided URL.
    val stringRequest = StringRequest(
        Request.Method.POST, url,
        Response.Listener<String> { processRequest(true) },
        Response.ErrorListener { processRequest(false) }
    )

    Log.i("sendIFTTTLightingRequest",  "Casey IFTTT URL: $url")

    // Add the request to the RequestQueue.
    queue.add(stringRequest)
}

