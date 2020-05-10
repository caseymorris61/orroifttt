package com.caseytmorris.sparkdirector.switcheditroom

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.database.DatabaseReference

class SwitchEditRoomViewModelFactory (
    private val roomControlId: String,
    private val homeDatabaseReference: DatabaseReference,
    private val application: Application): ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SwitchEditRoomViewModel::class.java)) {
            return SwitchEditRoomViewModel(roomControlId,homeDatabaseReference, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}