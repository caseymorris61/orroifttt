package com.caseytmorris.orroifttt.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [RoomControl::class], version = 2, exportSchema = false)
abstract class RoomControlDatabase : RoomDatabase() {

    abstract val roomDatabaseDao: RoomDatabaseDao

//    abstract val webhookApiKeyDatabaseDao: WebhookApiKeyDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: RoomControlDatabase? = null

        fun getInstance(context: Context): RoomControlDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        RoomControlDatabase::class.java,
                        "room_control_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}