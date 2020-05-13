package com.caseytmorris.sparkdirector

data class RoomFB(
    var roomName: String = "New Room",
    var turnOnWebhook: String = "turn_on",
    var turnOffWebhook: String = "turn_off",
    var setWebhook: String = "set",
    var webhookApiKey: String = "default_key",
    var roomUID: String = "randomlyGeneratedString"
)

data class RoomSchedule(
    var roomName: String = "UnknownRoomName",
    var roomUID: String = "randomlyGeneratedString",
    var level: Int = 0
)

//data class ScheduleAction(
//    var scheduleName : String,
//    var scheduleTimeHourInt : String,
//    var scheduleTimeMinInt : String,
//    var rooms : List<RoomSchedule>
//)