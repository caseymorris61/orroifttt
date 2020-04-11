package com.caseytmorris.orroifttt.switchaddroom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.caseytmorris.orroifttt.R
import com.caseytmorris.orroifttt.databinding.FragmentSwitchAddRoomBinding

class SwitchAddRoomFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding: FragmentSwitchAddRoomBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_switch_add_room, container, false
        )

        return binding.root
    }

}
