package com.caseytmorris.orroifttt.switchcontrol

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.caseytmorris.orroifttt.R
import com.caseytmorris.orroifttt.database.RoomControl
import com.caseytmorris.orroifttt.databinding.ListItemRoomControlBinding
import com.caseytmorris.orroifttt.sendIFTTTLightingRequest
import com.google.android.material.slider.Slider

class SwitchControlAdapter(val clickListener: RoomControlListener) : ListAdapter<RoomControl,SwitchControlAdapter.ViewHolder>(SwitchControlDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(clickListener,item)

    }

    class ViewHolder private constructor(val binding: ListItemRoomControlBinding): RecyclerView.ViewHolder(binding.root) {

        private lateinit var lightChangeListener : LightLevelSliderBarListener

        fun bind(clickListener: RoomControlListener,item: RoomControl) {
            lightChangeListener = LightLevelSliderBarListener(binding,item)
            binding.roomName.text = item.roomName
            binding.buttonTurnOnList.setBackgroundColor(binding.root.context.getColor(R.color.primaryLightColor))
            binding.buttonTurnOffList.setBackgroundColor(binding.root.context.getColor(R.color.primaryLightColor))

            binding.buttonTurnOnList.setOnClickListener {lightChangeListener.onButtonClickListener(it) }
            binding.buttonTurnOffList.setOnClickListener {lightChangeListener.offButtonClickListener(it)}
            binding.seekBar.addOnSliderTouchListener(lightChangeListener)

            binding.room = item
            binding.clickListener = clickListener
        }

        fun getRoom() : RoomControl = binding.room!!

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemRoomControlBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }


}

class LightLevelSliderBarListener(val binding: ListItemRoomControlBinding, val room: RoomControl) : Slider.OnSliderTouchListener {

    fun onButtonClickListener(view: View){
        sendIFTTTLightingRequest(room.turnOnWebhook, room.webhookApiKey,view.context, 200)
        binding.buttonTurnOnList.setBackgroundColor(binding.root.context.getColor(R.color.secondaryDarkColor))
        binding.buttonTurnOffList.setBackgroundColor(binding.root.context.getColor(R.color.primaryLightColor))
        binding.seekBar.value = 100F
    }

    fun offButtonClickListener(view: View){
        sendIFTTTLightingRequest(room.turnOffWebhook, room.webhookApiKey,view.context, 0)
        binding.buttonTurnOffList.setBackgroundColor(binding.root.context.getColor(R.color.primaryDarkColor))
        binding.buttonTurnOnList.setBackgroundColor(binding.root.context.getColor(R.color.primaryLightColor))
        binding.seekBar.value = 0F
    }

    fun sliderValueSet(view: View, level: Int){
        Log.i("Casey","Setting Level to $level based on slider")
        sendIFTTTLightingRequest(room.turnOffWebhook, room.webhookApiKey,view.context, level)
        binding.buttonTurnOffList.setBackgroundColor(binding.root.context.getColor(R.color.primaryLightColor))
        binding.buttonTurnOnList.setBackgroundColor(binding.root.context.getColor(R.color.secondaryDarkColor))

    }

    override fun onStartTrackingTouch(slider: Slider) {
        //Nothing to do for now
    }

    override fun onStopTrackingTouch(slider: Slider) {
        when {
            (slider.value) < 1 -> { offButtonClickListener(slider) }
            (slider.value) > 99 -> { onButtonClickListener(slider) }
            else -> {sliderValueSet(slider, slider.value.toInt())}
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

class RoomControlListener(val clickListener: (roomId: Long) -> Unit) {
    fun onClick(room: RoomControl) = clickListener(room.roomId)
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