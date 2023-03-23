package com.github.sdp.tarjetakuna.ui.webapi

import com.github.sdp.tarjetakuna.utils.Utils.Companion.printIfNotNullOrEmpty
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object WebApi {

    private const val magicUrl = "https://api.magicthegathering.io/v1/"
    private lateinit var magicApi: MagicApi

    fun getMagicApi(): MagicApi {
        if (!::magicApi.isInitialized) {
            magicApi = buildMagicApi()
        }
        return magicApi
    }

    private fun buildMagicApi(): MagicApi {
        // building request to API to get bored information
        val retrofit = Retrofit.Builder()
            .baseUrl(magicUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(MagicApi::class.java)
    }
}


data class DataCards(
    val cards: List<DataCard>
){
    override fun toString(): String {
        return cards.joinToString(separator = "\n\n")
    }
}

// DataCard that matches the JSON structure from the API
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

data class DataSets(
    val sets: List<DataSet>
){
    override fun toString(): String {
        return sets.joinToString(separator = "\n\n")
    }
}

data class DataSet(
    val code: String,
    val name: String,
    val type: String,
    val border: String?,
    val mkm_id: String?,
    val mkm_name: String?,
    val gathererCode: String,
    val magicCardsInfoCode: String,
    val releaseDate: String,
    val block: String,
    val onlineOnly: Boolean,
//    val booster: List<String>,
    val mkm_idExpansion: String,
    val mkm_nameExpansion: String
) {
    override fun toString(): String {
        return "DataSet(" +
                "code='$code', " +
                "name='$name', " +
                "type='$type', " +
                printIfNotNullOrEmpty(border, "border=") +
                printIfNotNullOrEmpty(mkm_id, "mkm_id=") +
                printIfNotNullOrEmpty(mkm_name, "mkm_name=") +
                printIfNotNullOrEmpty(gathererCode, "gathererCode=") +
                printIfNotNullOrEmpty(magicCardsInfoCode, "magicCardsInfoCode=") +
                printIfNotNullOrEmpty(releaseDate, "releaseDate=") +
                printIfNotNullOrEmpty(block, "block=") +
                printIfNotNullOrEmpty(onlineOnly, "onlineOnly=") +
//                "booster=$booster, " +
                printIfNotNullOrEmpty(mkm_idExpansion, "mkm_idExpansion=") +
                printIfNotNullOrEmpty(mkm_nameExpansion, "mkm_nameExpansion=") +
                ")"
    }
}
