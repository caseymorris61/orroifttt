package com.caseytmorris.sparkdirector.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RoomDatabaseDao {

    @Insert
    fun insertRoom(room: RoomControl)

    @Update
    fun updateRoom(room: RoomControl)

    @Delete
    fun deleteRoom(room: RoomControl)

    @Query("SELECT * from room_control_table WHERE roomId = :key")
    fun getRoom(key: Long): RoomControl?

    @Query("SELECT * from room_control_table ORDER BY roomName ASC")
    fun getAllRooms(): LiveData<List<RoomControl>>

    @Query("DELETE FROM room_control_table")
    fun clearRooms()
}

