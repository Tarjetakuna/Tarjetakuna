package com.github.sdp.tarjetakuna.database

import com.github.sdp.tarjetakuna.model.MagicCard

data class FBMagicCard(private val card: MagicCard, private val possession: FBCardPossesion) {

}
