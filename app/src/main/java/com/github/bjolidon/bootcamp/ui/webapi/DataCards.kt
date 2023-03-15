package com.github.bjolidon.bootcamp.ui.webapi

import com.github.bjolidon.bootcamp.model.MagicCard

data class DataCards(
    val cards: List<DataCard>
)

data class DataCard(
    val card: MagicCard
)
