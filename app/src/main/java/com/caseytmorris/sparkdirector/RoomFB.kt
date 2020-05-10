package com.caseytmorris.sparkdirector

data class RoomFB(
    var roomName: String = "New Room",
    var turnOnWebhook: String = "turn_on",
    var turnOffWebhook: String = "turn_off",
    var setWebhook: String = "set",
    var webhookApiKey: String = "default_key",
    var roomUID: String = "randomlyGeneratedString"
)