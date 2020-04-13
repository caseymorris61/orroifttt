package com.caseytmorris.orroifttt.switchcontrol

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
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
        private lateinit var room : RoomControl

        fun bind(item: RoomControl) {
            roomName.text = item.roomName
            onButton.setOnClickListener {
                sendIFTTTRequest(item.turnOnWebhook, it.context)
            }

            offButton.setOnClickListener {
                sendIFTTTRequest(item.turnOffWebhook, it.context)
            }
            room = item
        }

        fun getRoom() : RoomControl = room

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

abstract class SwipeToDeleteCallback(val context: Context) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    fun onMove(): Boolean {
        return false // We don't want support moving items up/down
    }

    // Let's draw our delete view
    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val deleteIcon = ContextCompat.getDrawable(context,R.drawable.ic_delete_icon)!!
        var colorDrawableBackground = ColorDrawable(ContextCompat.getColor(context, R.color.deleteColor))

        val itemView = viewHolder.itemView
        val iconMarginVertical = (viewHolder.itemView.height - deleteIcon.intrinsicHeight) / 2

        if (dX > 0) {
            colorDrawableBackground.setBounds(itemView.left, itemView.top, dX.toInt(), itemView.bottom)
            deleteIcon.setBounds(
                itemView.left + iconMarginVertical,
                itemView.top + iconMarginVertical,
                itemView.left + iconMarginVertical + deleteIcon.intrinsicWidth,
                itemView.bottom - iconMarginVertical
            )
        } else {
            colorDrawableBackground.setBounds(
                itemView.right + dX.toInt(),
                itemView.top,
                itemView.right,
                itemView.bottom
            )
            deleteIcon.setBounds(
                itemView.right - iconMarginVertical - deleteIcon.intrinsicWidth,
                itemView.top + iconMarginVertical,
                itemView.right - iconMarginVertical,
                itemView.bottom - iconMarginVertical
            )
            deleteIcon.level = 0
        }

        colorDrawableBackground.draw(c)

        c.save()

        if (dX > 0)
            c.clipRect(itemView.left, itemView.top, dX.toInt(), itemView.bottom)
        else
            c.clipRect(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)

        deleteIcon.draw(c)

        c.restore()

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}