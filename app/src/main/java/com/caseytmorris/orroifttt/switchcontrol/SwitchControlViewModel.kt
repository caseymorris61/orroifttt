package com.caseytmorris.orroifttt.switchcontrol

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.caseytmorris.orroifttt.database.RoomControl
import com.caseytmorris.orroifttt.database.RoomDatabaseDao
import com.caseytmorris.orroifttt.formatRoomsList

import kotlinx.coroutines.*

class SwitchControlViewModel (
    val roomControlDatabase: RoomDatabaseDao,
//    val apiDatabase: WebhookApiKeyDatabaseDao,
    application: Application)  : AndroidViewModel(application) {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

//    val webhookApiKey = apiDatabase.getApiKey()

//    val apiKeyString = Transformations.map(webhookApiKey) {
//        formatWebhookApiKey(it,application.resources)
//    }

    val rooms: LiveData<List<RoomControl>> = roomControlDatabase.getAllRooms()

    val roomsString = Transformations.map(rooms) {rooms ->
        formatRoomsList(rooms,application.resources)
    }

    private val _navigateToAddRoom = MutableLiveData<Long>()
    val navigateToAddRoom : LiveData<Long>
        get() = _navigateToAddRoom

    fun onAddRoomClicked(id: Long) {
        _navigateToAddRoom.value = id
    }

    fun onAddRoomNavigated() {
        _navigateToAddRoom.value = null
    }

    private suspend fun clear() {
        withContext(Dispatchers.IO) {
            roomControlDatabase.clearRooms()
//            apiDatabase.clearApiKey()
        }
    }


//    private suspend fun insertApiKey(webhookApiKey: WebhookApiKey) {
//        withContext(Dispatchers.IO){
//            apiDatabase.insertApiKey(webhookApiKey)
//        }
//    }

    fun onTestAddData() {
        uiScope.launch {
            Log.i("Casey","Trying to add data")
//            val apiKey = WebhookApiKey()
//            apiKey.apiKey = "TESTESTESTESTCASEY"
//
//            insertApiKey(apiKey)
//
//            val roomControl = RoomControl()
//            roomControl.roomName = "Attic"
//
//            insertRoom(roomControl)
//
//            val rC = RoomControl()
//            rC.roomName = "Bedroom"
//            rC.turnOnWebhook = "turn_on_bedroom"
//
//            insertRoom(rC)
//
//            val tonight = getTonight()
//            val hook = tonight?.turnOnWebhook
//            Log.i("Casey","Hook is: $hook")
        }
    }

    fun onTestClearAll() {
        uiScope.launch {
            Log.i("Casey","Trying to clear data")
//            clear()
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}
