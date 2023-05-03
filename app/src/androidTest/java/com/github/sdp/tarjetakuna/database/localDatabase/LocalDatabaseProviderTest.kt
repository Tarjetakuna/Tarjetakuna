package com.github.sdp.tarjetakuna.database.localDatabase

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdp.tarjetakuna.database.local.LocalDatabaseProvider
import com.github.sdp.tarjetakuna.ui.authentication.Authenticator
import com.github.sdp.tarjetakuna.ui.authentication.SignIn
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import java.util.*


@RunWith(AndroidJUnit4::class)
class LocalDatabaseProviderTest {

    @Before
    fun setUp() {
        val mockedAuth = mock(Authenticator::class.java)
        `when`(mockedAuth.isUserLoggedIn()).thenReturn(true)
        SignIn.setSignIn(mockedAuth)

        // close the database that could have been opened because of the previous tests
        LocalDatabaseProvider.closeDatabase("test")
        LocalDatabaseProvider.closeDatabase("test2")
        LocalDatabaseProvider.closeDatabase(LocalDatabaseProvider.CARDS_DATABASE_NAME)
    }

    @After
    fun tearDown() {
        LocalDatabaseProvider.closeDatabase("test")
        LocalDatabaseProvider.closeDatabase("test2")

    }

    @Test
    fun addDatabaseToProviderWorks() {
        LocalDatabaseProvider.setDatabase(ApplicationProvider.getApplicationContext(), "test")
        assertEquals("test", LocalDatabaseProvider.getDatabase("test")?.openHelper?.databaseName)
        LocalDatabaseProvider.closeDatabase("test")
    }

    @Test
    fun assignTwoDatabaseWorks() {
        LocalDatabaseProvider.setDatabase(ApplicationProvider.getApplicationContext(), "test")
        LocalDatabaseProvider.setDatabase(ApplicationProvider.getApplicationContext(), "test2")
        assertEquals(LocalDatabaseProvider.getDatabase("test2")?.openHelper?.databaseName, "test2")
        assertEquals(LocalDatabaseProvider.getDatabase("test")?.openHelper?.databaseName, "test")
        LocalDatabaseProvider.closeDatabase("test")
        LocalDatabaseProvider.closeDatabase("test2")
    }

    @Test
    fun assignTestDatabaseWorks() {
        LocalDatabaseProvider.setDatabase(
            ApplicationProvider.getApplicationContext(), "test", true
        )
        assertEquals(LocalDatabaseProvider.getDatabase("test") != null, true)
        LocalDatabaseProvider.closeDatabase("test")
    }
}
