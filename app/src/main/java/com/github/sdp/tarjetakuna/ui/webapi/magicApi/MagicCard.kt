package com.github.sdp.tarjetakuna.ui.webapi.magicApi

data class MagicCard(
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
) {
    override fun toString(): String {
        return "DataCard(" +
                "name='$name', " +
                "manaCost='$manaCost', " +
//                "cmc=$cmc, " +
                "colors=${colors[0]}, " +
//                "colorIdentity=$colorIdentity, " +
                "type='$type', " +
//                "types=$types, " +
//                "subtypes=$subtypes, " +
                "rarity='$rarity', " +
                "set='$set', " +
//                "setName='$setName', " +
                "text='$text', " +
//                "artist='$artist', " +
//                "number='$number', " +
                "power='$power', " +
                "toughness='$toughness', " +
//                "layout='$layout', " +
//                "multiverseid='$multiverseid', " +
                "imageUrl='$imageUrl', " +
//                "variations=$variations, " +
//                "foreignNames=$foreignNames, " +
//                "printings=$printings, " +
//                "originalText='$originalText', " +
//                "originalType='$originalType', " +
                "legalities=${legalities[0]}..., " +
                "id='$id'" +
                ")"
    }
}
