package com.github.sdp.tarjetakuna.ui.webapi.magicApi

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


/**
 * Contains the methods to access the database for the APIMagicCard
 */
@Dao
interface ApiMagicCardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCard(card: MagicCard)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCards(cards: List<MagicCard>)

    @Query("SELECT * FROM api_magic_cards")
    suspend fun getAllCards(): List<MagicCard>?

    // Get all the cards that match the name
    @Query("SELECT * FROM api_magic_cards WHERE name = :name")
    suspend fun getCardsByName(name: String): List<MagicCard>?

    // Get all the cards that match the set
    @Query("SELECT * FROM api_magic_cards WHERE `set` = :set")
    suspend fun getCardsBySet(set: String): List<MagicCard>?

    // Get all the cards that match the id, the id is unique so there will only be 1 card
    @Query("SELECT * FROM api_magic_cards WHERE id = :id")
    suspend fun getCardById(id: String): MagicCard?


}
