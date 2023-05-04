package com.github.sdp.tarjetakuna.database.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.github.sdp.tarjetakuna.database.DBMagicCard

@Database(entities = [DBMagicCard::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun magicCardDao(): MagicCardDao
}
