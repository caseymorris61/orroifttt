package com.caseytmorris.sparkdirector.schedule


import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.caseytmorris.sparkdirector.R
import com.caseytmorris.sparkdirector.RoomSchedule
import com.caseytmorris.sparkdirector.databinding.FragmentConfigureScheduleBinding
import com.caseytmorris.sparkdirector.databinding.ListItemRoomScheduleSetBinding
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class configureScheduleFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentConfigureScheduleBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_configure_schedule, container, false)


        binding.btnTime.setOnClickListener { showTimePickerDialog(it) }

        val query = FirebaseDatabase.getInstance()
            .reference
            .child("/user_homes/bhu4ZPRlOHaxLByE1smrkZrLAbq2/schedules/-M7FBXdx_H-v13FJ13tU/rooms")

        val options = FirebaseRecyclerOptions.Builder<RoomSchedule>()
            .setQuery(query, RoomSchedule::class.java)
            .setLifecycleOwner(this)
            .build()

        val adapter = RoomSelectAdapter(options)

        binding.roomScheduleList.adapter = adapter


        return binding.root
    }



    class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            // Use the current time as the default values for the picker
            val c = Calendar.getInstance()
            val hour = c.get(Calendar.HOUR_OF_DAY)
            val minute = c.get(Calendar.MINUTE)

            // Create a new instance of TimePickerDialog and return it
            return TimePickerDialog(activity, this, hour, minute, DateFormat.is24HourFormat(activity))
        }

        override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
            val textView = activity?.findViewById<TextView>(R.id.in_time)
            textView?.text = "Picked time: ${hourOfDay}:${minute}"
        }
    }

    fun showTimePickerDialog(v: View) {
        fragmentManager?.let {
            TimePickerFragment().show(it, "timePicker")
        }

    }
}

class RoomSelectAdapter(private val options: FirebaseRecyclerOptions<RoomSchedule>) : FirebaseRecyclerAdapter<RoomSchedule, RoomSelectAdapter.ViewHolder>(options) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: RoomSchedule) {
        holder.bind(model)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }


    class ViewHolder private constructor(val binding: ListItemRoomScheduleSetBinding) : RecyclerView.ViewHolder(binding.root) {


        fun bind(item: RoomSchedule) {
            binding.room = item
            binding.scheduleRoomName.text = item.roomName
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemRoomScheduleSetBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class RoomScheduleDiffCallback : DiffUtil.ItemCallback<RoomSchedule>() {
    override fun areItemsTheSame(oldItem: RoomSchedule, newItem: RoomSchedule): Boolean {
        return oldItem.roomUID == newItem.roomUID
    }

    override fun areContentsTheSame(oldItem: RoomSchedule, newItem: RoomSchedule): Boolean {
        return oldItem == newItem
    }
}