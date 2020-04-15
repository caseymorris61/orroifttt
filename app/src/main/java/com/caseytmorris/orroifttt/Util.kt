package com.caseytmorris.orroifttt

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.*
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName


fun sendIFTTTLightingRequest(event: String, webhookKey: String, context: Context, level: Int){
    sendIFTTTRequest(event,webhookKey,context, level) {
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

fun sendIFTTTRequest(event: String,webhookKey: String, context: Context, level:Int, processRequest: (Boolean) -> Unit) {
    val queue = Volley.newRequestQueue(context)
    val url = "https://maker.ifttt.com/trigger/$event/with/key/$webhookKey"

    val jsonLevel  = JsonSetLightClass(level)

    // Request a string response from the provided URL.
    val stringRequest = when(level) {
        in 1..99 -> {
            StringRequestWithJsonParameters(
                Request.Method.POST, url,
                Response.Listener<String> { processRequest(true) },
                Response.ErrorListener { processRequest(false) },
                Gson().toJson(jsonLevel)
            )
        }
        else -> {
            StringRequest(
                Request.Method.POST, url,
                Response.Listener<String> { processRequest(true) },
                Response.ErrorListener { processRequest(false) }
            )
        }
    }

    Log.i("sendIFTTTLightingRequest",  "Casey IFTTT URL: $url")

    // Add the request to the RequestQueue.
    queue.add(stringRequest)
}

fun sendIFTTTRequest(event: String,webhookKey: String, context: Context, processRequest: (Boolean) -> Unit) {
    sendIFTTTRequest(event,webhookKey,context, 200,processRequest)
}


data class JsonSetLightClass(@SerializedName("value1") val level: Int)

class StringRequestWithJsonParameters(method: Int, url: String,
                                      listener: Response.Listener<String>,
                                      errorListener: Response.ErrorListener?,
                                      val mRequestBody: String)
    : StringRequest(method,url,listener,errorListener){
    override fun getBodyContentType(): String {
        return "application/json; charset=utf-8"
    }

    @Throws(AuthFailureError::class)
    override fun getBody(): ByteArray? {
        return when(mRequestBody) {
            null -> null
            else -> mRequestBody.toByteArray(Charsets.UTF_8)
        }
    }

    override fun parseNetworkResponse(response: NetworkResponse?): Response<String> {
        var responseString = ""
        response?.let{
            responseString = response.statusCode.toString()
        }
        return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response))
    }
}

