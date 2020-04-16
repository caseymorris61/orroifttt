package com.caseytmorris.sparkdirector.switchcontrol

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.caseytmorris.sparkdirector.database.RoomControl
import com.caseytmorris.sparkdirector.database.RoomDatabaseDao

class SwitchControlViewModel (
    val roomControlDatabase: RoomDatabaseDao,
    application: Application)  : AndroidViewModel(application) {

    val rooms: LiveData<List<RoomControl>> = roomControlDatabase.getAllRooms()

    private val _navigateToEditRoom = MutableLiveData<Long>()
    val navigateToEditRoom : LiveData<Long>
        get() = _navigateToEditRoom

    fun onRoomControlClicked(id: Long) {
        Log.i("Casey","Clicked on room: $id")
        _navigateToEditRoom.value = id
    }
    fun doneNavigating() {
        _navigateToEditRoom.value = null
    }

}
