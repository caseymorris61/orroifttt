package com.caseytmorris.orroifttt.switchaddroom

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
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

    fun onSubmitClicked() {
        uiScope.launch {
            //log for testing
            Log.i("Casey","Add new room submitted")
            Log.i("Casey","${roomName.value}")
            Log.i("Casey","${turnOnKey.value}")
            Log.i("Casey","${turnOffKey.value}")
            //insert the room

            //navigate back
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