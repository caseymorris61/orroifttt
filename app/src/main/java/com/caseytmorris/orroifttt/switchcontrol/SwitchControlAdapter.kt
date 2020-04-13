package com.caseytmorris.orroifttt.switchcontrol

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.caseytmorris.orroifttt.R
import com.caseytmorris.orroifttt.database.RoomControl

class SwitchControlAdapter : ListAdapter<RoomControl,SwitchControlAdapter.ViewHolder>(SwitchControlDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)

    }

    class ViewHolder private constructor(itemView: View): RecyclerView.ViewHolder(itemView) {
        val roomName: TextView = itemView.findViewById(R.id.room_name)
        val onButton: Button = itemView.findViewById(R.id.button_turn_on_list)
        val offButton: Button = itemView.findViewById(R.id.button_turn_off_list)

        fun bind(item: RoomControl) {
            roomName.text = item.roomName
            onButton.setOnClickListener {
                sendIFTTTRequest(item.turnOnWebhook, it.context)
            }

            offButton.setOnClickListener {
                sendIFTTTRequest(item.turnOffWebhook, it.context)
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_room_control, parent, false)
                return ViewHolder(view)
            }
        }

        private fun sendIFTTTRequest(event:String, context: Context){
            val queue = Volley.newRequestQueue(context)
            val url = "https://maker.ifttt.com/trigger/$event/with/key/${context.getString(R.string.webhooks_key)}"

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

            Log.i("sendIFTTTRequest",  "Casey IFTTT URL: $url")

            // Add the request to the RequestQueue.
            queue.add(stringRequest)
        }
    }


}

class SwitchControlDiffCallback : DiffUtil.ItemCallback<RoomControl>() {
    override fun areItemsTheSame(oldItem: RoomControl, newItem: RoomControl): Boolean {
        return oldItem.roomId == newItem.roomId
    }

    override fun areContentsTheSame(oldItem: RoomControl, newItem: RoomControl): Boolean {
        return oldItem == newItem
    }
}