package com.caseytmorris.orroifttt.switchcontrol

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.caseytmorris.orroifttt.R
import com.caseytmorris.orroifttt.database.RoomControlDatabase
import com.caseytmorris.orroifttt.databinding.FragmentSwitchControlBinding


class SwitchControl : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding: FragmentSwitchControlBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_switch_control, container, false)

        val application = requireNotNull(this.activity).application

        val roomDataSource = RoomControlDatabase.getInstance(application).roomDatabaseDao

//        val webhookDataSource = RoomControlDatabase.getInstance(application).webhookApiKeyDatabaseDao

        val viewModelFactory = SwitchControlViewModelFactory(roomDataSource,application)

        val switchControlViewModel = ViewModelProviders.of(
            this, viewModelFactory).get(SwitchControlViewModel::class.java)

        binding.setLifecycleOwner(this)

        binding.viewModel = switchControlViewModel

        binding.buttonAddRoom.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_switch_control_fragment_to_switchAddRoomFragment)
        )

//        binding.textViewRooms.text = switchControlViewModel.roomsString.value
//
//        binding.textViewWebapi.text = switchControlViewModel.apiKeyString.value

        val adapter = SwitchControlAdapter()
        binding.roomList.adapter = adapter

        switchControlViewModel.rooms.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        return binding.root
    }

}

