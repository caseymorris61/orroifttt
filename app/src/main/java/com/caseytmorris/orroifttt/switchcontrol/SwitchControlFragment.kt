package com.caseytmorris.orroifttt.switchcontrol

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.caseytmorris.orroifttt.R
import com.caseytmorris.orroifttt.databinding.FragmentSwitchControlBinding


class SwitchControl : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding: FragmentSwitchControlBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_switch_control, container, false)

        binding.turnOnButton.setOnClickListener {
            Log.i("TurnOnClick",  "IFTTT On Clicked")
            sendIFTTTRequest(getString(R.string.turn_on_webhook))
        }

        binding.turnOffButton.setOnClickListener {
            Log.i("TurnOffClick","IFTTT Off Clicked")
            sendIFTTTRequest(getString(R.string.turn_off_webhook))
        }

        binding.buttonAddRoom.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_switch_control_fragment_to_switchAddRoomFragment)
        )


        return binding.root
    }

    private fun sendIFTTTRequest(event:String){
        val queue = Volley.newRequestQueue(context)
        val url = "https://maker.ifttt.com/trigger/$event/with/key/${getString(R.string.webhooks_key)}"

        // Request a string response from the provided URL.
        val stringRequest = StringRequest(
            Request.Method.POST, url,
            Response.Listener<String> { response ->
                // Display the first 500 characters of the response string.
                Toast.makeText(context,"Success! Lighting action in progress!",
                    Toast.LENGTH_SHORT).show()
            },
            Response.ErrorListener {
                Toast.makeText(context,"Error Processing Lighting Request!",
                    Toast.LENGTH_SHORT).show()
            }
        )

        Log.i("sendIFTTTRequest",  "IFTTT URL: $url")

        // Add the request to the RequestQueue.
        queue.add(stringRequest)
    }
}

