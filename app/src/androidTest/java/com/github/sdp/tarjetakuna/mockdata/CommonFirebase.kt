package com.github.sdp.tarjetakuna.mockdata

import com.github.sdp.tarjetakuna.database.CardPossession
import com.github.sdp.tarjetakuna.database.DBMagicCard
import com.google.gson.Gson

/**
 * This is a common class that can be used to get json for firebase
 */
object CommonFirebase {
    val goodFirebase = mapOf(
        "cards" to mapOf(
            GoodFirebaseAttributes.card1 to Gson().toJson(GoodFirebaseAttributes.cardObject1)
        ),
        "usernames" to mapOf(
            GoodFirebaseAttributes.username1 to GoodFirebaseAttributes.email1,
        ),
        "users" to mapOf(
            GoodFirebaseAttributes.username1 to mapOf(
                "location" to mapOf(
                    "lat" to GoodFirebaseAttributes.lat1,
                    "long" to GoodFirebaseAttributes.long1
                ),
                "wanted" to mapOf(
                    GoodFirebaseAttributes.card1 to mapOf(
                        "lastUpdated" to GoodFirebaseAttributes.lastUpdate,
                        "quantity" to 1,
                    )
                ),
                "owned" to mapOf(
                    GoodFirebaseAttributes.card1 to mapOf(
                        "lastUpdated" to GoodFirebaseAttributes.lastUpdate,
                        "quantity" to 1,
                    )
                )
            )
        )
    )

    object GoodFirebaseAttributes {
        const val username1 = "keke"
        const val email1 = "keke@gmail.com"
        const val lat1 = 11.0
        const val long1 = 14.0
        const val card1 = "M15_43"
        const val lastUpdate = 1685118635621L
        val cardObject1 = DBMagicCard(CommonMagicCard.aeronautTinkererCard, CardPossession.OWNED, 0)
    }
}
