package com.github.sdp.tarjetakuna.mockdata

import com.github.sdp.tarjetakuna.database.CardPossession
import com.github.sdp.tarjetakuna.model.Coordinates
import com.github.sdp.tarjetakuna.model.User

/**
 * This is a common class that can be used to get [User] objects
 */
object CommonUser {

    /**
     * This is a valid [User]
     */
    val testUser = User(
        "test@gmail.com",
        "testAccount",
        listOf(
            CommonMagicCard.aeronautTinkererCard.toDBMagicCard(CardPossession.OWNED),
            CommonMagicCard.venomousHierophantCard.toDBMagicCard(CardPossession.WANTED),
            CommonMagicCard.solemnOfferingCard.toDBMagicCard(CardPossession.OWNED)
        ),
        Coordinates(0.0f, 0.0f)
    )

}
