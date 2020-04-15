package com.caseytmorris.orroifttt.utils

import android.content.Context
import android.util.Log
import com.android.volley.*
import com.android.volley.toolbox.*
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

class IFTTTRequestSender private constructor(context: Context) {
    companion object {
        @Volatile
        private var INSTANCE: IFTTTRequestSender? = null

        fun getInstance(context: Context) =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: IFTTTRequestSender(context).also {
                    INSTANCE = it
                }
            }
    }

    val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }

    fun <T> addToRequestQueue(req: Request<T>) {
        requestQueue.add(req)
    }

    fun sendIFTTTRequest(
        event: String,
        webhookKey: String,
        level: Int,
        processRequest: (Boolean) -> Unit
    ) {
        val url = "https://maker.ifttt.com/trigger/$event/with/key/$webhookKey"

        val jsonLevel = JsonSetLightClass(level)

        // Request a string response from the provided URL.
        val stringRequest = when (level) {
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

        Log.i("sendIFTTTLightingRequest", "Casey IFTTT URL: $url")

        // Add the request to the RequestQueue.
        addToRequestQueue(stringRequest)
    }

    fun sendIFTTTRequest(
        event: String,
        webhookKey: String,
        processRequest: (Boolean) -> Unit
    ) {
        sendIFTTTRequest(event, webhookKey, 200, processRequest)
    }


    data class JsonSetLightClass(@SerializedName("value1") val level: Int)

    inner class StringRequestWithJsonParameters(
        method: Int, url: String,
        listener: Response.Listener<String>,
        errorListener: Response.ErrorListener?,
        val mRequestBody: String
    ) : StringRequest(method, url, listener, errorListener) {
        override fun getBodyContentType(): String {
            return "application/json; charset=utf-8"
        }

        @Throws(AuthFailureError::class)
        override fun getBody(): ByteArray? {
            return when (mRequestBody) {
                null -> null
                else -> mRequestBody.toByteArray(Charsets.UTF_8)
            }
        }

        override fun parseNetworkResponse(response: NetworkResponse?): Response<String> {
            var responseString = ""
            response?.let {
                responseString = response.statusCode.toString()
            }
            return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response))
        }
    }
}

