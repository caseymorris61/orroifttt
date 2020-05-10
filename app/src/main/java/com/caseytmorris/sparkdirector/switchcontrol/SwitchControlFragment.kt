package com.caseytmorris.sparkdirector.switchcontrol

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.caseytmorris.sparkdirector.R
import com.caseytmorris.sparkdirector.database.RoomControlDatabase
import com.caseytmorris.sparkdirector.databinding.FragmentSwitchControlBinding
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.caseytmorris.sparkdirector.RoomFB
import com.google.firebase.database.FirebaseDatabase




class SwitchControl : Fragment() {

    private lateinit var  adapter : SwitchControlAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding: FragmentSwitchControlBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_switch_control, container, false)

        val application = requireNotNull(this.activity).application

        val homePath = "user_homes/bhu4ZPRlOHaxLByE1smrkZrLAbq2/home"

        val roomDataSource = RoomControlDatabase.getInstance(application).roomDatabaseDao

        val viewModelFactory = SwitchControlViewModelFactory(roomDataSource,application)

        val switchControlViewModel = ViewModelProviders.of(
            this, viewModelFactory).get(SwitchControlViewModel::class.java)

        binding.setLifecycleOwner(this)

        binding.viewModel = switchControlViewModel

        binding.buttonAddRoom.setOnClickListener {
            this.findNavController().navigate(
                SwitchControlDirections.actionSwitchControlFragmentToSwitchAddRoomFragment(homePath)
            )
        }


        val query = FirebaseDatabase.getInstance()
            .reference
            .child(homePath)

        val options = FirebaseRecyclerOptions.Builder<RoomFB>()
            .setQuery(query, RoomFB::class.java)
            .setLifecycleOwner(this)
            .build()

        adapter = SwitchControlAdapter( RoomControlListener { roomId ->
                switchControlViewModel.onRoomControlClicked(roomId)
            }
            , options)

        binding.roomList.adapter = adapter

//        switchControlViewModel.rooms.observe(viewLifecycleOwner, Observer {
//            it?.let {
//                adapter.submitList(it)
//                when (it.isEmpty() ) {
//                    true -> binding.emptyListText.visibility = View.VISIBLE
//                    else -> binding.emptyListText.visibility = View.GONE
//                }
//
//            }
//        })

        switchControlViewModel.navigateToEditRoom.observe(this, Observer { roomId ->
            roomId?.let {
                this.findNavController().navigate(
                    SwitchControlDirections.actionSwitchControlFragmentToSwitchEditRoomFragment(roomId,homePath))
                switchControlViewModel.doneNavigating()
            }
        })



        return binding.root
    }
}

