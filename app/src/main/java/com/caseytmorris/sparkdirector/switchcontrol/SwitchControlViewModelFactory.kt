package com.caseytmorris.sparkdirector.switchcontrol

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.caseytmorris.sparkdirector.database.RoomDatabaseDao

class SwitchControlViewModelFactory (
    private val roomControlDatabase: RoomDatabaseDao,
    private val application:Application): ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SwitchControlViewModel::class.java)) {
            return SwitchControlViewModel(roomControlDatabase, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
