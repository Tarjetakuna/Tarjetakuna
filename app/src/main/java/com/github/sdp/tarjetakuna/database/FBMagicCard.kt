package com.github.sdp.tarjetakuna.database

import com.github.sdp.tarjetakuna.model.MagicCard

data class FBMagicCard(val card: MagicCard, val possession: FBCardPossesion) {
}
