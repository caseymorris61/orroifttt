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
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import com.caseytmorris.sparkdirector.R
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class configureScheduleFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_configure_schedule, container, false)

        val view: View? = inflater.inflate(R.layout.fragment_configure_schedule, container, false)

        val setTimeButton: Button? = view?.findViewById(R.id.btn_time)
        setTimeButton?.setOnClickListener { showTimePickerDialog(it) }

        return view
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
