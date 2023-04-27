package com.github.sdp.tarjetakuna.database.localDatabase

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdp.tarjetakuna.database.local.LocalDatabaseProvider
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LocalDatabaseProviderTest {

    @After
    fun tearDown() {
        LocalDatabaseProvider.closeDatabase()
    }

    @Test
    fun addDatabaseToProviderWorks() {
        LocalDatabaseProvider.setDatabase(ApplicationProvider.getApplicationContext(), "test")
        assert(LocalDatabaseProvider.getDatabase() != null)
    }

    @Test
    fun assignTwoDatabaseDoesNotWork() {
        LocalDatabaseProvider.setDatabase(ApplicationProvider.getApplicationContext(), "test2")
        LocalDatabaseProvider.setDatabase(ApplicationProvider.getApplicationContext(), "test3")
        assert(LocalDatabaseProvider.getDatabase() != null)
        assert(LocalDatabaseProvider.getDatabase()?.openHelper?.databaseName == "test2")
    }

    @Test
    fun assignTestDatabaseWorks() {
        LocalDatabaseProvider.setDatabase(
            ApplicationProvider.getApplicationContext(), "", true
        )
        assert(LocalDatabaseProvider.getDatabase() != null)
    }
}
