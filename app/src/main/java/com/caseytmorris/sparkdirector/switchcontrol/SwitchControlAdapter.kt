package com.caseytmorris.sparkdirector.switchcontrol

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.caseytmorris.sparkdirector.utils.IFTTTRequestSender
import com.caseytmorris.sparkdirector.R
import com.caseytmorris.sparkdirector.RoomFB
import com.caseytmorris.sparkdirector.databinding.ListItemRoomControlBinding
import com.google.android.material.slider.Slider
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions


class SwitchControlAdapter(val clickListener: RoomControlListener, private val options: FirebaseRecyclerOptions<RoomFB>) : FirebaseRecyclerAdapter<RoomFB,SwitchControlAdapter.ViewHolder>(options) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: RoomFB) {
        holder.bind(clickListener,model)
    }

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

        fun bind(clickListener: RoomControlListener,item: RoomFB) {
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

class LightLevelSliderBarListener(val binding: ListItemRoomControlBinding, val room: RoomFB) : Slider.OnSliderTouchListener {

    fun onButtonClickListener(view: View){
        sendIFTTTLightingRequest(view,room.turnOnWebhook,100)
        binding.buttonTurnOnList.setBackgroundColor(binding.root.context.getColor(com.caseytmorris.sparkdirector.R.color.secondaryDarkColor))
        binding.buttonTurnOffList.setBackgroundColor(binding.root.context.getColor(com.caseytmorris.sparkdirector.R.color.primaryLightColor))
        binding.seekBar.value = 100F
    }

    fun offButtonClickListener(view: View){
        sendIFTTTLightingRequest(view,room.turnOffWebhook,0)
        binding.buttonTurnOffList.setBackgroundColor(binding.root.context.getColor(com.caseytmorris.sparkdirector.R.color.primaryDarkColor))
        binding.buttonTurnOnList.setBackgroundColor(binding.root.context.getColor(com.caseytmorris.sparkdirector.R.color.primaryLightColor))
        binding.seekBar.value = 0F
    }

    private fun sliderValueSet(view: View, level: Int){
        sendIFTTTLightingRequest(view,room.setWebhook,level)
        binding.buttonTurnOffList.setBackgroundColor(binding.root.context.getColor(com.caseytmorris.sparkdirector.R.color.primaryLightColor))
        binding.buttonTurnOnList.setBackgroundColor(binding.root.context.getColor(com.caseytmorris.sparkdirector.R.color.secondaryDarkColor))

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

class RoomControlListener(val clickListener: (roomName: String) -> Unit) {
    fun onClick(room: RoomFB) = clickListener(room.roomUID)
}