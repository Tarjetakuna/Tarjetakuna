package com.github.sdp.tarjetakuna.ui.webapi.magicApi

import androidx.room.Entity
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson

/**
 * Data class for a MagicCard (need to match the doc in api.magicthegathering.io/v1/)
 */
@Entity(tableName = "api_magic_cards", primaryKeys = ["id"])
data class MagicCard(
    val name: String,
    val manaCost: String,
    val cmc: Int,
    @field:TypeConverters(StringListConverter::class)
    val colors: List<String>,
    @field:TypeConverters(StringListConverter::class)
    val colorIdentity: List<String>,
    val type: String,
    @field:TypeConverters(StringListConverter::class)
    val types: List<String>,
    @field:TypeConverters(StringListConverter::class)
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
    val multiverseid: String?,
    val imageUrl: String?,
    @field:TypeConverters(StringListConverter::class)
    val variations: List<String>?,
    @field:TypeConverters(ForeignNameListConverter::class)
    val foreignNames: List<ForeignName>,
    @field:TypeConverters(StringListConverter::class)
    val printings: List<String>,
    val originalText: String,
    val originalType: String,
    @field:TypeConverters(LegalitiesListConverter::class)
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
                "multiverseid='$multiverseid', " +
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

/**
 * Converters to be able to store string lists in the database
 */
class StringListConverter {
    @TypeConverter
    fun fromString(value: String?): List<String> {
        if (value == null) return emptyList()
        return value.split(",")
    }

    @TypeConverter
    fun fromString(value: List<String>?): String {
        if (value == null) return ""
        return value.joinToString(",")
    }
}

/**
 * Converters to be able to store ForeignName lists in the database
 */
class ForeignNameListConverter {
    @TypeConverter
    fun fromString(value: String?): List<ForeignName> {
        if (value == null) return emptyList()
        return ForeignName.fromJsonToList(value)
    }

    @TypeConverter
    fun fromList(list: List<ForeignName>?): String {
        if (list == null) return ""
        return Gson().toJson(list)
    }
}

/**
 * Converters to be able to store Legalities lists in the database
 */
class LegalitiesListConverter {
    @TypeConverter
    fun fromString(value: String?): List<Legalities> {
        if (value == null) return emptyList()
        return Legalities.fromJsonToList(value)
    }

    @TypeConverter
    fun fromList(list: List<Legalities>?): String {
        if (list == null) return ""
        return Gson().toJson(list)
    }
}
