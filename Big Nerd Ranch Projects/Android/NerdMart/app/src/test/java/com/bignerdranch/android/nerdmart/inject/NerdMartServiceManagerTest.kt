package com.bignerdranch.android.nerdmart.inject

import com.bignerdranch.android.nerdmart.model.DataStore
import com.bignerdranch.android.nerdmart.model.service.NerdMartServiceManager
import com.bignerdranch.android.nerdmartservice.service.NerdMartServiceInterface
import com.bignerdranch.android.nerdmartservice.service.payload.User
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.junit.Assert.assertTrue
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class NerdMartServiceManagerTest {

    // Object under test
    private lateinit var nerdMartServiceManager: NerdMartServiceManager

    // Parameters for NerdMartServiceManager
    private lateinit var nerdMartService: NerdMartServiceInterface
    private lateinit var dataStore: DataStore
    private lateinit var scheduler: Scheduler

    @Before
    fun setup() {
        nerdMartService = mock(NerdMartServiceInterface::class.java)
        dataStore = mock(DataStore::class.java)
        // Can't mock scheduler so return trampoline to run on test thread
        scheduler = Schedulers.trampoline()
        nerdMartServiceManager = NerdMartServiceManager(
                serviceInterface = nerdMartService,
                dataStore = dataStore,
                observationScheduler = scheduler
        )
    }

    @Test
    fun testAuthenticateMethodReturnsFalseWithInvalidCredentials() {
        // Return failure observable to simulate invalid credentials
        val invalidUser = User.NO_USER
        val failObservable = Observable.just(invalidUser)
        `when`(nerdMartService.authenticate(any(), any()))
                .thenReturn(failObservable)
        nerdMartServiceManager.authenticate("johnnydoe", "WRONGPASSWORD")
                .test()
                .also { assertTrue(it.awaitTerminalEvent()) }
                .assertValueCount(1)
                .assertValue(false)
        verify(dataStore).cachedUser = invalidUser
    }

    @Test
    fun testAuthenticateMethodReturnsTrueWithValidCredentials() {
        val validUser = mock(User::class.java)
        val successObservable = Observable.just(validUser)
        `when`(nerdMartService.authenticate(any(), any()))
                .thenReturn(successObservable)
        nerdMartServiceManager.authenticate("johnnydoe", "pizza")
                .test()
                .also { assertTrue(it.awaitTerminalEvent()) }
                .assertValueCount(1)
                .assertValue(true)
        verify(dataStore).cachedUser = validUser
    }

}