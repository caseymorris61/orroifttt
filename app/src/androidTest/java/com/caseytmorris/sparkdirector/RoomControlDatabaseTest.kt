package com.caseytmorris.sparkdirector

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.caseytmorris.sparkdirector.database.RoomControl
import com.caseytmorris.sparkdirector.database.RoomControlDatabase
import com.caseytmorris.sparkdirector.database.RoomDatabaseDao
import org.junit.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class RoomControlDatabaseTest {

    private lateinit var roomDao: RoomDatabaseDao
    private lateinit var db: RoomControlDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, RoomControlDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        roomDao = db.roomDatabaseDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetNight() {
        val room = RoomControl()
        roomDao.insertRoom(room)
        val tonight = roomDao.getTonight()
        assertEquals(tonight?.turnOnWebhook, "turn_on")
    }
}