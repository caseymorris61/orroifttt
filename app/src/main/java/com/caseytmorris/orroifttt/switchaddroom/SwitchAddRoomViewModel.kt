package com.caseytmorris.orroifttt.switchaddroom

import android.app.Application
import android.util.Log
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import com.caseytmorris.orroifttt.R
import com.caseytmorris.orroifttt.database.RoomControl
import com.caseytmorris.orroifttt.database.RoomDatabaseDao
import kotlinx.coroutines.*

class SwitchAddRoomViewModel  (
    val roomControlDatabase: RoomDatabaseDao,
    application: Application
)  : AndroidViewModel(application) {
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val roomName = MutableLiveData<String>()
    val turnOnKey = MutableLiveData<String>()
    val turnOffKey = MutableLiveData<String>()

    fun onSubmitClicked(view: View) {
        uiScope.launch {
            //insert the room
            val rc = RoomControl()
            rc.roomName = roomName?.value ?: "invalid"
            rc.turnOnWebhook = turnOnKey?.value ?: "invalid"
            rc.turnOffWebhook = turnOffKey?.value ?: "invalid"
            insertRoom(rc)

            //navigate back
            view.findNavController().navigate(R.id.action_switchAddRoomFragment_to_switchControlFragment)

        }
    }


    private suspend fun insertRoom(room: RoomControl) {
        withContext(Dispatchers.IO){
            roomControlDatabase.insertRoom(room)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}