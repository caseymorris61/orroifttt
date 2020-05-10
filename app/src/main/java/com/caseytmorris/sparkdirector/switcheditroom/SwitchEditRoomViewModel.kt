package com.caseytmorris.sparkdirector.switcheditroom

import android.app.AlertDialog
import android.app.Application
import android.content.DialogInterface
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat.getColor
import androidx.navigation.findNavController
import com.caseytmorris.sparkdirector.RoomFB
import com.caseytmorris.sparkdirector.switchaddroom.SwitchViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext




class SwitchEditRoomViewModel (
    private val roomControlId: String = "",
    private val homeDatabaseReference: DatabaseReference,
    application: Application
)  : SwitchViewModel(application) {

    private var rc = RoomFB()

    private val roomListener = object : ValueEventListener {
        override fun onCancelled(p0: DatabaseError) {
            // Getting Room failed, log a message
            Log.w("Casey", "roomListener:onCancelled", p0.toException())
        }

        override fun onDataChange(p0: DataSnapshot) {
            roomName.value = p0.child("roomName").value.toString()
            turnOnKey.value = p0.child("turnOnWebhook").value.toString()
            turnOffKey.value = p0.child("turnOffWebhook").value.toString()
            setLevelKey.value = p0.child("setWebhook").value.toString()
            _webhookApiKeyLiveData.value = p0.child("webhookApiKey").value.toString()
        }
    }

    init {
        uiScope.launch {
            getRoom(roomControlId)?.addValueEventListener(roomListener)
        }
    }

    fun onSaveClicked(view: View) {
        uiScope.launch {
            //update the room
            rc.roomName = roomName?.value ?: "invalid"
            rc.turnOnWebhook = turnOnKey?.value ?: "invalid"
            rc.turnOffWebhook = turnOffKey?.value ?: "invalid"
            rc.webhookApiKey = _webhookApiKeyLiveData?.value ?: defaultStringApiKey
            rc.setWebhook = setLevelKey?.value ?: "invalid"
            rc.roomUID = roomControlId
            updateRoom(rc)
            
            navBackToControlFragment(view)
        }
    }

    fun onDeleteClicked(view: View) {
        val dialogBuilder = AlertDialog.Builder(view.context)

        dialogBuilder.setMessage("Delete ${roomName.value}?")
            .setCancelable(false)
            .setPositiveButton("Delete"){dialog,id ->
                onDeleteConfirmed(view)
            }
            .setNegativeButton("Cancel") {dialog, id ->
                dialog.cancel()
            }

        val alert = dialogBuilder.create()
        alert.show()
        alert.getButton(DialogInterface.BUTTON_POSITIVE)?.setTextColor(getColor(view.context,
            com.caseytmorris.sparkdirector.R.color.deleteColor))

    }

    private fun navBackToControlFragment(view: View) {
        //navigate back
        view.findNavController().navigate(com.caseytmorris.sparkdirector.R.id.action_switchEditRoomFragment_to_switchControlFragment)
    }

    private fun onDeleteConfirmed(view: View){
        Log.i("Casey","Confirmed Delete of: ${roomName.value}")
        deleteRoom(roomControlId)
        navBackToControlFragment(view)
    }

    private suspend fun updateRoom(room: RoomFB) {
        withContext(Dispatchers.IO) {
            homeDatabaseReference.child(roomControlId).setValue(room)
        }
    }

    private suspend fun getRoom(id: String) : DatabaseReference? {
        return withContext(Dispatchers.IO){
            homeDatabaseReference.child(id)
        }
    }

    private suspend fun delete(roomId: String){
        withContext(Dispatchers.IO) {
            homeDatabaseReference.child(roomId).removeValue()
        }
    }

    fun deleteRoom(roomId: String) {
        uiScope.launch {
            delete(roomId)
        }
    }
}