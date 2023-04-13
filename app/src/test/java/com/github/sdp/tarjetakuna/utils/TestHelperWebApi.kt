package com.github.sdp.tarjetakuna.utils

import com.github.sdp.tarjetakuna.ui.webapi.magicApi.*
import com.google.gson.Gson

object TestHelperWebApi {

    private val gson = Gson()

    fun getCardByIdResponse(): MagicCard {
        val json =
            ResourceHelper.ResourceHelper.loadString("magic_webapi_cardById_386616_response.json")
        return gson.fromJson(json, MagicApiCard::class.java).card
    }

    fun getCardByNameResponse(): List<MagicCard> {
        val json =
            ResourceHelper.ResourceHelper.loadString("magic_webapi_cardByName_Ancester'sChosen_response.json")
        return gson.fromJson(json, MagicApiCards::class.java).cards
    }

    fun getCardBySetResponse(): List<MagicCard> {
        val json =
            ResourceHelper.ResourceHelper.loadString("magic_webapi_cardBySet_KTK_response.json")
        return gson.fromJson(json, MagicApiCards::class.java).cards
    }

    fun getCardsResponse(): List<MagicCard> {
        val json = ResourceHelper.ResourceHelper.loadString("magic_webapi_cards_response.json")
        return gson.fromJson(json, MagicApiCards::class.java).cards
    }

    fun getSetByIdResponse(): MagicSet {
        val json =
            ResourceHelper.ResourceHelper.loadString("magic_webapi_setBySet_KTK_response.json")
        return gson.fromJson(json, MagicApiSet::class.java).set
    }

    fun getSetsResponse(): List<MagicSet> {
        val json = ResourceHelper.ResourceHelper.loadString("magic_webapi_sets_response.json")
        return gson.fromJson(json, MagicApiSets::class.java).sets
    }

}
