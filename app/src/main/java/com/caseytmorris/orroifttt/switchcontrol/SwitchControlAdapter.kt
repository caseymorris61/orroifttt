package com.caseytmorris.orroifttt.switchcontrol

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.caseytmorris.orroifttt.utils.IFTTTRequestSender
import com.caseytmorris.orroifttt.R
import com.caseytmorris.orroifttt.database.RoomControl
import com.caseytmorris.orroifttt.databinding.ListItemRoomControlBinding
import com.google.android.material.slider.Slider

class SwitchControlAdapter(val clickListener: RoomControlListener) : ListAdapter<RoomControl,SwitchControlAdapter.ViewHolder>(SwitchControlDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(clickListener,item)

    }

    class ViewHolder private constructor(val binding: ListItemRoomControlBinding)
        : RecyclerView.ViewHolder(binding.root){

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
        sendIFTTTLightingRequest(view,room.turnOnWebhook,100)
        binding.buttonTurnOnList.setBackgroundColor(binding.root.context.getColor(R.color.secondaryDarkColor))
        binding.buttonTurnOffList.setBackgroundColor(binding.root.context.getColor(R.color.primaryLightColor))
        binding.seekBar.value = 100F
    }

    fun offButtonClickListener(view: View){
        sendIFTTTLightingRequest(view,room.turnOffWebhook,0)
        binding.buttonTurnOffList.setBackgroundColor(binding.root.context.getColor(R.color.primaryDarkColor))
        binding.buttonTurnOnList.setBackgroundColor(binding.root.context.getColor(R.color.primaryLightColor))
        binding.seekBar.value = 0F
    }

    private fun sliderValueSet(view: View, level: Int){
        sendIFTTTLightingRequest(view,room.setWebhook,level)
        binding.buttonTurnOffList.setBackgroundColor(binding.root.context.getColor(R.color.primaryLightColor))
        binding.buttonTurnOnList.setBackgroundColor(binding.root.context.getColor(R.color.secondaryDarkColor))

    }

    private fun processLightingChangeResponse(result: Boolean){
        when (result) {
            true -> {
                Toast.makeText(
                    binding.root.context, "Success! Lighting action in progress!",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else -> {
                Toast.makeText(
                    binding.root.context, "Error Processing Lighting Request!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun sendIFTTTLightingRequest(view: View,webhook:String,level: Int){
        IFTTTRequestSender.getInstance(view.context)
            .sendIFTTTRequest(webhook, room.webhookApiKey, level){processLightingChangeResponse(it)}
    }

    override fun onStartTrackingTouch(slider: Slider) {
        //Assume lights are being turned on or are already on
        binding.buttonTurnOffList.setBackgroundColor(binding.root.context.getColor(R.color.primaryLightColor))
        binding.buttonTurnOnList.setBackgroundColor(binding.root.context.getColor(R.color.secondaryDarkColor))
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