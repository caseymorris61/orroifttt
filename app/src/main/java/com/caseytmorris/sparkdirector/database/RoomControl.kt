package com.caseytmorris.sparkdirector.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "room_control_table")
data class RoomControl(
    @PrimaryKey(autoGenerate = true)
    var roomId: Long = 0L,
    var roomName: String = "New Room $roomId",
    var turnOnWebhook: String = "turn_on",
    var turnOffWebhook: String = "turn_off",
    var setWebhook: String = "set",
    var webhookApiKey: String = "default_key"
)

//@Entity(tableName = "webhook_api_storage_table")
//data class WebhookApiKey (
//    @PrimaryKey(autoGenerate = true)
//    var keyId: Long = 0L,
//    var apiKey: String = ""
//)