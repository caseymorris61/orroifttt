package com.caseytmorris.orroifttt.switcheditroom

import android.app.AlertDialog
import android.app.Application
import android.content.DialogInterface
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat.getColor
import androidx.navigation.findNavController
import com.caseytmorris.orroifttt.R
import com.caseytmorris.orroifttt.database.RoomControl
import com.caseytmorris.orroifttt.database.RoomDatabaseDao
import com.caseytmorris.orroifttt.switchaddroom.SwitchViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SwitchEditRoomViewModel (
    private val roomControlId: Long = 0L,
    private val roomControlDatabase: RoomDatabaseDao,
    application: Application
)  : SwitchViewModel(application) {

    private lateinit var rc: RoomControl
    init {
        uiScope.launch {
            rc = getRoom(roomControlId) ?: RoomControl()
            roomName.value = rc.roomName
            turnOnKey.value = rc.turnOnWebhook
            turnOffKey.value = rc.turnOffWebhook
            setLevelKey.value = rc.setWebhook
            _webhookApiKeyLiveData.value = rc.webhookApiKey
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
        alert.getButton(DialogInterface.BUTTON_POSITIVE)?.setTextColor(getColor(view.context,R.color.deleteColor))

    }

    private fun navBackToControlFragment(view: View) {
        //navigate back
        view.findNavController().navigate(R.id.action_switchEditRoomFragment_to_switchControlFragment)
    }

    private fun onDeleteConfirmed(view: View){
        Log.i("Casey","Confirmed Delete of: ${roomName.value}")
        deleteRoom(roomControlId)
        navBackToControlFragment(view)
    }

    private suspend fun updateRoom(room: RoomControl) {
        withContext(Dispatchers.IO) {
            roomControlDatabase.updateRoom(room)
        }
    }

    private suspend fun getRoom(id: Long) : RoomControl? {
        return withContext(Dispatchers.IO){
             roomControlDatabase.getRoom(id)
        }
    }

    private suspend fun delete(roomId: Long){
        withContext(Dispatchers.IO) {
            roomControlDatabase.getRoom(roomId)?.let {
                roomControlDatabase.deleteRoom(it)
            }
        }
    }

    fun deleteRoom(roomId: Long) {
        uiScope.launch {
            delete(roomId)
        }
    }
}