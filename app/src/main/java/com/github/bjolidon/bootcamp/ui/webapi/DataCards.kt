package com.github.bjolidon.bootcamp.ui.webapi

data class DataCards(
    val cards: List<DataCard>
)

data class DataCard(
    val name: String,
    val manaCost: String,
    val cmc: Int,
    val colors: List<String>,
    val colorIdentity: List<String>,
    val type: String,
    val types: List<String>,
    val subtypes: List<String>,
    val rarity: String,
    val set: String,
    val setName: String,
    val text: String,
    val artist: String,
    val number: String,
    val power: String,
    val toughness: String,
    val layout: String,
    val multiverseid: String,
    val imageUrl: String,
    val variations: List<String>,
    val foreignNames: List<ForeignName>,
    val printings: List<String>,
    val originalText: String,
    val originalType: String,
    val legalities: List<Legalities>,
    val id: String
)

data class ForeignName(
    val name: String,
    val text: String,
    val type: String,
    val flavor: String,
    val imageUrl: String,
    val language: String,
    val multiverseid: Int
)

data class Legalities(
    val format: String,
    val legality: String
)