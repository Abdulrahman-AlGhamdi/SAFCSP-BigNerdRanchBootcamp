package com.ss.restaloca.recentSearch

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.ss.restaloca.TestUtils.androidGetOrAwaitValue
import com.ss.restaloca.common.Businesse
import com.ss.restaloca.common.Category
import com.ss.restaloca.common.Coordinates
import com.ss.restaloca.common.Location
import com.ss.restaloca.database.RestaLocaDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class RecentSearchDaoTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var database: RestaLocaDatabase
    private lateinit var dao: RecentSearchDao
    private lateinit var recentSearch: Businesse

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RestaLocaDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.recentSearchDao()
        recentSearch = Businesse(
            "le98572kwfnwf786hwpf4857393",
            listOf(Category("Spicy")),
            Coordinates(0.0, 0.0),
            "0557040201",
            25.0,
            "https://image.com",
            false,
            Location("Saudi Arabia", "Riyadh"),
            "$$$",
            "Mama Mia",
            "+966557040201",
            5.0,
            100,
            "https://image.com"
        )
    }

    @Test
    fun addRecentSearch() = runBlocking {
        dao.addRecentSearch(recentSearch)
        val data = dao.getAllRecentSearchById().androidGetOrAwaitValue()
        assert(data.isNotEmpty())
        assert(data.size == 1)
    }

    @Test
    fun deleteAllRecentSearch() = runBlocking {
        dao.addRecentSearch(recentSearch)
        dao.deleteAllRecentSearch()
        val data = dao.getAllRecentSearchById().androidGetOrAwaitValue()
        assert(data.isEmpty())
    }
}