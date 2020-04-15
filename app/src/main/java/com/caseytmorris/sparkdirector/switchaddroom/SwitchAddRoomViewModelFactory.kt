package com.caseytmorris.sparkdirector.switchaddroom

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.caseytmorris.sparkdirector.database.RoomDatabaseDao

class SwitchAddRoomViewModelFactory (
    private val roomControlDatabase: RoomDatabaseDao,
    private val application:Application): ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SwitchAddRoomViewModel::class.java)) {
            return SwitchAddRoomViewModel(roomControlDatabase, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}