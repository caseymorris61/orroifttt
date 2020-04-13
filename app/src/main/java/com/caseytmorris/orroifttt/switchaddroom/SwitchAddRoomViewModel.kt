package com.caseytmorris.orroifttt.switchaddroom

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
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
    val webhookApiKeyLiveData = MutableLiveData<String>()

    var defaultStringApiKey = ""
    var webhook_api_key = ""
    private var sharedPref: SharedPreferences

    init {
        sharedPref = application?.getSharedPreferences(
                application.getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        defaultStringApiKey = application.getString(R.string.default_webhook_api_key)
        webhook_api_key = sharedPref?.getString(application.getString(R.string.saved_webhook_api_key), defaultStringApiKey) ?: defaultStringApiKey

        if(webhook_api_key != defaultStringApiKey) {
            //Passing a valid string to use api key
            webhookApiKeyLiveData.value = webhook_api_key
            Log.i("Casey","Using API Key: ${webhookApiKeyLiveData.value}")
        }
        else {
            Log.i("Casey","No valid api key in shared preferences. Required")
        }
    }

    fun onSubmitClicked(view: View) {
        uiScope.launch {
            //insert the room
            val rc = RoomControl()
            rc.roomName = roomName?.value ?: "invalid"
            rc.turnOnWebhook = turnOnKey?.value ?: "invalid"
            rc.turnOffWebhook = turnOffKey?.value ?: "invalid"
            rc.webhookApiKey = webhookApiKeyLiveData?.value ?: defaultStringApiKey
            insertRoom(rc)

            with(sharedPref.edit()) {
                putString(getApplication<Application>().getString(R.string.saved_webhook_api_key), rc.webhookApiKey)
                commit()
            }

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