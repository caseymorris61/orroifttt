package com.caseytmorris.orroifttt.switchaddroom

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import com.caseytmorris.orroifttt.R
import com.caseytmorris.orroifttt.database.RoomControl
import com.caseytmorris.orroifttt.database.RoomDatabaseDao
import com.caseytmorris.orroifttt.sendIFTTTRequest
import kotlinx.coroutines.*

enum class API_KEY_VALIDATION_STATE {UNKNOWN,FAILED,PASS}

open class SwitchViewModel(application: Application) : AndroidViewModel(application) {
    var viewModelJob = Job()
    val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val roomName = MutableLiveData<String>()
    val turnOnKey = MutableLiveData<String>()
    val turnOffKey = MutableLiveData<String>()
    val setLevelKey = MutableLiveData<String>()

    val _webhookApiKeyLiveData = MutableLiveData<String>()
    val webhookApiKeyLiveData : LiveData<String>
        get() = _webhookApiKeyLiveData

    protected val _apiKeyValidated = MutableLiveData<API_KEY_VALIDATION_STATE>()
    val apiKeyValidated : LiveData<API_KEY_VALIDATION_STATE>
        get() = _apiKeyValidated

    var defaultStringApiKey = ""
    var webhook_api_key = ""
    var sharedPref: SharedPreferences

    init {
        sharedPref = application?.getSharedPreferences(
            application.getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        defaultStringApiKey = application.getString(R.string.default_webhook_api_key)
        webhook_api_key = sharedPref?.getString(application.getString(R.string.saved_webhook_api_key), defaultStringApiKey) ?: defaultStringApiKey

        _apiKeyValidated.value = API_KEY_VALIDATION_STATE.UNKNOWN
    }

    fun onValidateClicked(view: View) {

        uiScope.launch {
            val keyToValidate = _webhookApiKeyLiveData?.value ?: defaultStringApiKey
            validateApiKey(keyToValidate,view.context)
        }

    }

    protected suspend fun validateApiKey(key:String, context: Context){
        withContext(Dispatchers.IO) {
            sendIFTTTRequest("testtesttest",key,context) { response ->
                processIFTTTValidationResponse(response)
            }
        }
    }

    fun processIFTTTValidationResponse(response: Boolean) {
        when(response) {
            true -> {
                _apiKeyValidated.value = API_KEY_VALIDATION_STATE.PASS

            }
            else -> {
                _apiKeyValidated.value = API_KEY_VALIDATION_STATE.FAILED
            }
        }
    }

    fun apiKeyChanged() {
        _apiKeyValidated.value = API_KEY_VALIDATION_STATE.UNKNOWN
    }
}

class SwitchAddRoomViewModel  (
    private val roomControlDatabase: RoomDatabaseDao,
    application: Application
)  : SwitchViewModel(application) {

    init {
        if(webhook_api_key != defaultStringApiKey) {
            //Passing a valid string to use api key
            _webhookApiKeyLiveData.value = webhook_api_key
            Log.i("Casey","Using API Key: ${_webhookApiKeyLiveData.value}")
            _apiKeyValidated.value = API_KEY_VALIDATION_STATE.PASS
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
            rc.setWebhook = setLevelKey?.value ?: "invalid"
            rc.webhookApiKey = _webhookApiKeyLiveData?.value ?: defaultStringApiKey
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