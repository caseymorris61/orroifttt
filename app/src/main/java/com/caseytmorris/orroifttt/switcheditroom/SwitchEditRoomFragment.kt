package com.caseytmorris.orroifttt.switcheditroom


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.caseytmorris.orroifttt.R
import com.caseytmorris.orroifttt.database.RoomControlDatabase
import com.caseytmorris.orroifttt.databinding.FragmentSwitchEditRoomBinding
import com.caseytmorris.orroifttt.switchaddroom.API_KEY_VALIDATION_STATE
import com.caseytmorris.orroifttt.switchaddroom.SwitchAddRoomViewModel

/**
 * A simple [Fragment] subclass.
 */
class SwitchEditRoomFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentSwitchEditRoomBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_switch_edit_room, container, false
        )

        val application = requireNotNull(this.activity).application

        val arguments = SwitchEditRoomFragmentArgs.fromBundle(arguments!!)

        val roomDataSource = RoomControlDatabase.getInstance(application).roomDatabaseDao

        val viewModelFactory = SwitchEditRoomViewModelFactory(arguments.roomControlIdKey,roomDataSource,application)

        val switchEditRoomViewModel = ViewModelProviders.of(
            this, viewModelFactory).get(SwitchEditRoomViewModel::class.java)

        binding.setLifecycleOwner(this)

        binding.viewModel = switchEditRoomViewModel

        switchEditRoomViewModel.webhookApiKeyLiveData.observe(this, Observer {
            switchEditRoomViewModel.apiKeyChanged()
        })

        switchEditRoomViewModel.apiKeyValidated.observe(this, Observer { newState ->
            when (newState) {
                API_KEY_VALIDATION_STATE.PASS -> {
                    binding.saveRoom.isEnabled = true
                    binding.validateButton.isEnabled = false
                }
                API_KEY_VALIDATION_STATE.FAILED -> {
                    Toast.makeText(context,"Error Validating Webhook API Key",
                        Toast.LENGTH_SHORT).show()
                    binding.saveRoom.isEnabled = false
                    binding.validateButton.isEnabled = true
                }
                else -> {
                    binding.saveRoom.isEnabled = false
                    binding.validateButton.isEnabled = true
                }

            }
        })

        return binding.root
    }


}
