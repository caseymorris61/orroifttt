package com.caseytmorris.orroifttt.switchcontrol

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.caseytmorris.orroifttt.database.RoomControl
import com.caseytmorris.orroifttt.database.RoomDatabaseDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext

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


    fun onTestAddData() {
        Log.i("Casey","Trying to add data")

    }

    fun onTestClearAll() {
        Log.i("Casey","Trying to clear data")
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}
