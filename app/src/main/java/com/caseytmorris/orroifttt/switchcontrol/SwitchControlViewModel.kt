package com.caseytmorris.orroifttt.switchcontrol

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.caseytmorris.orroifttt.database.RoomControl
import com.caseytmorris.orroifttt.database.RoomDatabaseDao
import kotlinx.coroutines.*

class SwitchControlViewModel (
    val roomControlDatabase: RoomDatabaseDao,
    application: Application)  : AndroidViewModel(application) {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val rooms: LiveData<List<RoomControl>> = roomControlDatabase.getAllRooms()

    private suspend fun clear() {
        withContext(Dispatchers.IO) {
            roomControlDatabase.clearRooms()
        }
    }

    private suspend fun delete(room: RoomControl){
        withContext(Dispatchers.IO) {
            roomControlDatabase.deleteRoom(room)
        }
    }

    fun deleteRoom(room: RoomControl) {
        uiScope.launch {
            Log.i("Casey","Want to remove room name ${room.roomName}")
            delete(room)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}
