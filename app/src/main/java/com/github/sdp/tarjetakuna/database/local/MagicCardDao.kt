package com.github.sdp.tarjetakuna.database.local

import androidx.room.*

/**
 * Represents a Magic card data access object.
 */

@Dao
interface MagicCardDao {
    @Query("SELECT * FROM magic_card")
    suspend fun getAllCards(): List<MagicCardEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCard(card: MagicCardEntity)
    
    @Delete
    suspend fun deleteCard(card: MagicCardEntity)

    @Query("DELETE FROM magic_card")
    suspend fun deleteAllCards()
}
