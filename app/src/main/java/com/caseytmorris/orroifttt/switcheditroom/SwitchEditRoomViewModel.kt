package com.caseytmorris.orroifttt.switcheditroom

import android.app.Application
import android.view.View
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

            //navigate back
            view.findNavController().navigate(R.id.action_switchEditRoomFragment_to_switchControlFragment)

        }
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
}