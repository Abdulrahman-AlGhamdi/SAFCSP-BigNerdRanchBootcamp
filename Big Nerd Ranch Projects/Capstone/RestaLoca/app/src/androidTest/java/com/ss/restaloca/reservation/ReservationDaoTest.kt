package com.ss.restaloca.reservation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.ss.restaloca.TestUtils.androidGetOrAwaitValue
import com.ss.restaloca.database.RestaLocaDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class ReservationDaoTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var database: RestaLocaDatabase
    private lateinit var dao: ReservationDao
    private lateinit var reservation: ReservationModel

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RestaLocaDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.reservationDao()
        reservation = ReservationModel(
            "le98572kwfnwf786hwpf4857393",
            "Indian",
            24.5532,
            -44.3316254788,
            "https://image.com",
            "Saudi Arabia, Riyadh",
            "SS",
            "0557040201",
            5.0,
            100,
            "Mama Mia",
            "This is not how you hold a pistol",
            "Friday, 21/9/2020",
            "000000".toInt(),
            true
        )
    }

    @Test
    fun addReservation() = runBlocking {
        dao.addReservation(reservation)
        val data = dao.getAllReservation().androidGetOrAwaitValue()
        assert(data.isNotEmpty())
        assert(data.size == 1)
    }

    @Test
    fun deleteReservation() = runBlocking {
        dao.deleteReservation(reservation)
        val data = dao.getAllReservation().androidGetOrAwaitValue()
        assert(data.isEmpty())
    }

    @After
    fun tearDown() {
        database.close()
    }
}