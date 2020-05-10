package com.caseytmorris.sparkdirector.switchcontrol

import android.app.Activity
import android.content.Intent
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
import androidx.navigation.fragment.findNavController
import com.caseytmorris.sparkdirector.R
import com.caseytmorris.sparkdirector.databinding.FragmentSwitchControlBinding
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.caseytmorris.sparkdirector.RoomFB
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*


class SwitchControl : Fragment() {

    private lateinit var  adapter : SwitchControlAdapter

    private lateinit var firebaseDatabase : FirebaseDatabase
    private lateinit var homeDatabaseReference: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener

    private var userUID: String = ANONYMOUS
    private lateinit var homePath: String

    companion object {
        const val RC_SIGN_IN = 622
        const val ANONYMOUS = "unknown_user"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding: FragmentSwitchControlBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_switch_control, container, false)

        val application = requireNotNull(this.activity).application

        firebaseDatabase = FirebaseDatabase.getInstance()
        homeDatabaseReference = firebaseDatabase.reference.child("user_homes")

        firebaseAuth = FirebaseAuth.getInstance()

        homePath = makeHomePath(ANONYMOUS)

        val viewModelFactory = SwitchControlViewModelFactory(application)

        val switchControlViewModel = ViewModelProviders.of(
            this, viewModelFactory).get(SwitchControlViewModel::class.java)

        binding.setLifecycleOwner(this)

        binding.viewModel = switchControlViewModel

        binding.buttonAddRoom.setOnClickListener {
            this.findNavController().navigate(
                SwitchControlDirections.actionSwitchControlFragmentToSwitchAddRoomFragment(homePath)
            )
        }

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

        authStateListener = FirebaseAuth.AuthStateListener { authFirebase ->
            val user = authFirebase.currentUser
            if (user != null) {
                //user is signed in
                onSignedInInitialize(user,switchControlViewModel, binding)
            } else {
                //user is not signed in
                onSignedOutCleanup()
                binding.roomList.adapter = null
                startActivityForResult(
                    AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setIsSmartLockEnabled(false)
                        .setTosAndPrivacyPolicyUrls("http://caseytmorris.com/sparkdirector/terms-of-service.html",
                            "http://caseytmorris.com/sparkdirector/privacy-policy.html")
                        .setTheme(R.style.AppTheme)
                        .setLogo(R.drawable.ic_launcher_bulb_signin)
                        .setAvailableProviders(
                            Arrays.asList<AuthUI.IdpConfig>(
                                AuthUI.IdpConfig.GoogleBuilder().build(),
                                AuthUI.IdpConfig.EmailBuilder().build()
                            )
                        )
                        .build(),
                    RC_SIGN_IN
                )
            }
        }

        return binding.root
    }

    private fun onSignedInInitialize( currentUser: FirebaseUser,
        switchControlViewModel: SwitchControlViewModel,
        binding: FragmentSwitchControlBinding
    ) {
        userUID = currentUser.uid

        homePath = makeHomePath(userUID)
        val query = FirebaseDatabase.getInstance()
            .reference
            .child(homePath)

        val options = FirebaseRecyclerOptions.Builder<RoomFB>()
            .setQuery(query, RoomFB::class.java)
            .setLifecycleOwner(this)
            .build()

        adapter = SwitchControlAdapter(RoomControlListener { roomId ->
            switchControlViewModel.onRoomControlClicked(roomId)
        }
            , options)

        binding.roomList.adapter = adapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(this.context, "Sign In Result OK", Toast.LENGTH_SHORT).show()
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this.context, "Sign In Cancelled", Toast.LENGTH_SHORT).show()
                activity?.finish()
            }
        }
    }
    private fun onSignedOutCleanup() {
        //unset username if saved
        //detach listener
        Log.i("Casey","Cleanup on signed out please")
        userUID = ANONYMOUS
        homePath = makeHomePath(userUID)
    }

    override fun onResume() {
        super.onResume()
        firebaseAuth.addAuthStateListener(authStateListener)
    }

    override fun onPause() {
        super.onPause()
        firebaseAuth.removeAuthStateListener(authStateListener)
    }

    fun makeHomePath(s: String) = "user_homes/$s/home"
}

