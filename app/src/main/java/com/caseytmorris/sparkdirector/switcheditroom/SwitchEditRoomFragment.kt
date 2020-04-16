package com.caseytmorris.sparkdirector.switcheditroom


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.caseytmorris.sparkdirector.R
import com.caseytmorris.sparkdirector.database.RoomControlDatabase
import com.caseytmorris.sparkdirector.databinding.FragmentSwitchEditRoomBinding
import com.caseytmorris.sparkdirector.switchaddroom.API_KEY_VALIDATION_STATE

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
