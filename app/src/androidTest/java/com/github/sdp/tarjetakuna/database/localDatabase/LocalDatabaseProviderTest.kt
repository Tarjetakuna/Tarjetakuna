package com.github.sdp.tarjetakuna.database.localDatabase

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdp.tarjetakuna.database.local.LocalDatabaseProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.powermock.api.mockito.PowerMockito
import java.util.*


@RunWith(AndroidJUnit4::class)
class LocalDatabaseProviderTest {

    @Before
    fun setUp() {
        PowerMockito.mockStatic(FirebaseAuth::class.java)
        val mockedAuth = Mockito.mock(FirebaseAuth::class.java)
        Mockito.`when`(FirebaseAuth.getInstance()).thenReturn(mockedAuth)
        println(Firebase.auth.currentUser)

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
