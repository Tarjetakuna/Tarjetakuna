package com.github.sdp.tarjetakuna.database.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [MagicCardEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun magicCardDao(): MagicCardDao
}
