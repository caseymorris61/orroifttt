package com.caseytmorris.sparkdirector.switchaddroom

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.caseytmorris.sparkdirector.R
import com.caseytmorris.sparkdirector.database.RoomControlDatabase
import com.caseytmorris.sparkdirector.databinding.FragmentSwitchAddRoomBinding
import com.google.firebase.database.FirebaseDatabase


class SwitchAddRoomFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding: FragmentSwitchAddRoomBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_switch_add_room, container, false
        )

        val application = requireNotNull(this.activity).application

        val arguments = SwitchAddRoomFragmentArgs.fromBundle(arguments!!)

        val viewModelFactory = SwitchAddRoomViewModelFactory(
            FirebaseDatabase.getInstance().reference.child(arguments.homeReferencePath),
            application)

        val switchAddRoomViewModel = ViewModelProviders.of(
            this, viewModelFactory).get(SwitchAddRoomViewModel::class.java)

        binding.setLifecycleOwner(this)

        binding.viewModel = switchAddRoomViewModel

        switchAddRoomViewModel.webhookApiKeyLiveData.observe(this, Observer {
            switchAddRoomViewModel.apiKeyChanged()
        })

        switchAddRoomViewModel.apiKeyValidated.observe(this, Observer { newState ->
            when (newState) {
                API_KEY_VALIDATION_STATE.PASS -> {
                    Log.i("Casey","Validation Passed")
                    binding.addRoomButton.isEnabled = true
                    binding.validateButton.isEnabled = false
                }
                API_KEY_VALIDATION_STATE.FAILED -> {
                    Log.i("Casey","Validation Failed")
                    //Prompt message that it failed to validate
                    binding.addRoomButton.isEnabled = false
                    binding.validateButton.isEnabled = true
                }
                else -> {
                    Log.i("Casey","Validation Unknown")
                    binding.addRoomButton.isEnabled = false
                    binding.validateButton.isEnabled = true
                }

            }
        })
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item!!,
            view!!.findNavController())
                || super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.overflow_menu, menu)
    }
}
