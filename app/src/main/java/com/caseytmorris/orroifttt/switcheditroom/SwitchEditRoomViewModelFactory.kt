package com.caseytmorris.orroifttt.switcheditroom

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.caseytmorris.orroifttt.database.RoomDatabaseDao

class SwitchEditRoomViewModelFactory (
    private val roomControlId: Long,
    private val roomControlDatabase: RoomDatabaseDao,
    private val application: Application): ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SwitchEditRoomViewModel::class.java)) {
            return SwitchEditRoomViewModel(roomControlId,roomControlDatabase, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}