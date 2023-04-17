package com.github.sdp.tarjetakuna.database.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

/**
 * Represents a Magic card data access object.
 */

@Dao
interface MagicCardDao {
    @Query("SELECT * FROM magic_card")
    suspend fun getAllCards(): List<MagicCardEntity>

    @Insert
    suspend fun insertCard(card: MagicCardEntity)

    @Delete
    suspend fun deleteCard(card: MagicCardEntity)

    @Query("DELETE FROM magic_card")
    suspend fun deleteAllCards()
}
