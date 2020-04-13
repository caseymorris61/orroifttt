package com.caseytmorris.orroifttt.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface RoomDatabaseDao {

    @Insert
    fun insertRoom(room: RoomControl)

    @Update
    fun updateRoom(room: RoomControl)

    @Query("SELECT * from room_control_table WHERE roomId = :key")
    fun getRoom(key: Long): RoomControl?

    @Query("SELECT * from room_control_table ORDER BY roomName ASC")
    fun getAllRooms(): LiveData<List<RoomControl>>

    @Query("SELECT * FROM room_control_table ORDER BY roomId DESC LIMIT 1")
    fun getTonight(): RoomControl?

    @Query("DELETE FROM room_control_table")
    fun clearRooms()
}

//@Dao
//interface WebhookApiKeyDatabaseDao {
//    @Insert
//    fun insertApiKey(apiKey: WebhookApiKey)
//
//    @Update
//    fun updateApiKey(apiKey: WebhookApiKey)
//
//    @Query("SELECT * from webhook_api_storage_table ORDER BY apiKey DESC LIMIT 1")
//    fun getApiKey(): LiveData<WebhookApiKey>
//
//    @Query("DELETE FROM webhook_api_storage_table")
//    fun clearApiKey()
//}
