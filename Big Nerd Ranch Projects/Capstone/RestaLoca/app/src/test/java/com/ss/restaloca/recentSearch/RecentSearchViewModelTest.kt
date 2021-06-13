package com.ss.restaloca.recentSearch

import android.os.Build.VERSION_CODES.P
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.ss.restaloca.common.Businesse
import com.ss.restaloca.database.RestaLocaDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [P])
class RecentSearchViewModelTest {

    @Mock
    lateinit var repository: RecentSearchRepository

    @Mock
    lateinit var database: RestaLocaDatabase

    @Mock
    lateinit var business: Businesse

    @InjectMocks
    lateinit var recentSearchViewModel: RecentSearchViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        val context = InstrumentationRegistry.getInstrumentation().context
        database = RestaLocaDatabase(context)
        repository = RecentSearchRepository(database)
        recentSearchViewModel = RecentSearchViewModel(repository)
        business = Mockito.mock(Businesse::class.java)
    }

    @Test
    fun testAddRecentSearch() {
        recentSearchViewModel.addRecentSearch(business)
        recentSearchViewModel.getAllRecentSearchById().observeForever {
            Assert.assertEquals(it, null)
        }
    }

    @Test
    fun testDeleteAllRecentSearch() {
        recentSearchViewModel.addRecentSearch(business)
        recentSearchViewModel.deleteAllRecentSearch()
        recentSearchViewModel.getAllRecentSearchById().observeForever {
            Assert.assertEquals(it, null)
        }
    }

    @Test
    fun testGetAllRecentSearchById() {
        GlobalScope.launch {
            recentSearchViewModel.getAllRecentSearchById().observeForever {
                Assert.assertEquals(it, null)
            }
        }
    }
}