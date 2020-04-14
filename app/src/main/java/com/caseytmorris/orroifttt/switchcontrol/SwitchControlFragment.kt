package com.caseytmorris.orroifttt.switchcontrol

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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
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

        val viewModelFactory = SwitchControlViewModelFactory(roomDataSource,application)

        val switchControlViewModel = ViewModelProviders.of(
            this, viewModelFactory).get(SwitchControlViewModel::class.java)

        binding.setLifecycleOwner(this)

        binding.viewModel = switchControlViewModel

        binding.buttonAddRoom.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_switch_control_fragment_to_switchAddRoomFragment)
        )

        val adapter = SwitchControlAdapter( RoomControlListener { roomId ->
            switchControlViewModel.onRoomControlClicked(roomId)
        })

        binding.roomList.adapter = adapter

        val swipeHandler = object : SwipeToDeleteCallback(application.applicationContext) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val vh = viewHolder as SwitchControlAdapter.ViewHolder
                binding.viewModel?.deleteRoom(vh.getRoom())
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.roomList)

        switchControlViewModel.rooms.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
                when (it.isEmpty() ) {
                    true -> binding.emptyListText.visibility = View.VISIBLE
                    else -> binding.emptyListText.visibility = View.GONE
                }

            }
        })

        switchControlViewModel.navigateToEditRoom.observe(this, Observer { roomId ->
            roomId?.let {
                this.findNavController().navigate(
                    SwitchControlDirections.actionSwitchControlFragmentToSwitchEditRoomFragment(roomId))
                switchControlViewModel.doneNavigating()
            }
        })

        return binding.root
    }

}

