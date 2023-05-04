package com.github.sdp.tarjetakuna.mockdata

import com.github.sdp.tarjetakuna.ui.webapi.magicApi.MagicApiCard

/**
 * Common [CommonMagicApiCard] to use in tests
 */
object CommonMagicApiCard {

    val mockCard = MagicApiCard(
        arena_id = MockCard.validArenaId,
        id = MockCard.validId,
        lang = MockCard.validLang,
        mtgo_id = MockCard.validMtgoId,
        mtgo_foil_id = MockCard.validMtgoFoilId,
        multiverse_ids = MockCard.validMultiverseIds,
        tcgplayer_id = MockCard.validTcgplayerId,
        tcgplayer_etched_id = MockCard.validTcgplayerEtchedId,
        cardmarket_id = MockCard.validCardmarketId,
        oracle_id = MockCard.validOracleId,
        prints_search_uri = MockCard.validPrintsSearchUri,
        rulings_uri = MockCard.validRulingsUri,
        scryfall_uri = MockCard.validScryfallUri,
        uri = MockCard.validUri,
        all_parts = MockCard.validAllParts,
        card_faces = MockCard.validCardFaces,
        cmc = MockCard.validCmc,
        colors = MockCard.validColors,
        color_identity = MockCard.validColorIdentity,
        color_indicator = MockCard.validColorIndicator,
        edhrec_rank = MockCard.validEdhrecRank,
        hand_modifier = MockCard.validHandModifier,
        layout = MockCard.validLayout,
        legalities = MockCard.validLegalities,
        life_modifier = MockCard.validLifeModifier,
        loyalty = MockCard.validLoyalty,
        mana_cost = MockCard.validManaCost,
        name = MockCard.validName,
        oracle_text = MockCard.validOracleText,
        oversized = MockCard.validOversized,
        power = MockCard.validPower,
        reserved = MockCard.validReserved,
        toughness = MockCard.validToughness,
        type_line = MockCard.validTypeLine,
        artist = MockCard.validArtist,
        booster = MockCard.validBooster,
        border_color = MockCard.validBorderColor,
        card_back_id = MockCard.validCardBackId,
        collector_number = MockCard.validCollectorNumber,
        content_warning = MockCard.validContentWarning,
        digital = MockCard.validDigital,
        flavor_name = MockCard.validFlavorName,
        flavor_text = MockCard.validFlavorText,
        frame_effects = MockCard.validFrameEffects,
        frame = MockCard.validFrame,
        full_art = MockCard.validFullArt,
        games = MockCard.validGames,
        highres_image = MockCard.validHighresImage,
        illustration_id = MockCard.validIllustrationId,
        image_status = MockCard.validImageStatus,
        image_uris = MockCard.validImageUris,
        prices = MockCard.validPrices,
        printed_name = MockCard.validPrintedName,
        printed_text = MockCard.validPrintedText,
        printed_type_line = MockCard.validPrintedTypeLine,
        promo = MockCard.validPromo,
        promo_types = MockCard.validPromoTypes,
        purchase_uris = MockCard.validPurchaseUris,
        rarity = MockCard.validRarity,
        related_uris = MockCard.validRelatedUris,
        released_at = MockCard.validReleasedAt,
        reprint = MockCard.validReprint,
        scryfall_set_uri = MockCard.validScryfallSetUri,
        set_name = MockCard.validSetName,
        set_search_uri = MockCard.validSetSearchUri,
        set_type = MockCard.validSetType,
        set_uri = MockCard.validSetUri,
        set = MockCard.validSet,
        story_spotlight = MockCard.validStorySpotlight,
        textless = MockCard.validTextless,
        variation = MockCard.validVariation,
        variation_of = MockCard.validVariationOf,
        watermark = MockCard.validWatermark,
        keywords = MockCard.validKeywords,
        produced_mana = MockCard.validProducedMana,
        penny_rank = MockCard.validPennyRank,
        attraction_lights = MockCard.validAttractionLights,
        finishes = MockCard.validFinishes,
        security_stamp = MockCard.validSecurityStamp,
        set_id = MockCard.validSetId,
    )

    object MockCard {
        const val validArenaId = 1
        const val validId = "0574973b-a0d1-4e17-9ed7-42a98d25bb8d"
        const val validLang = "en"
        const val validMtgoId = 1
        const val validMtgoFoilId = 1
        val validMultiverseIds = listOf(1, 2, 3)
        const val validTcgplayerId = 1
        const val validTcgplayerEtchedId = 1
        const val validCardmarketId = 1
        const val validOracleId = "0574973b-a0d1-4e17-9ed7-42a98d25bb8d"
        const val validPrintsSearchUri = "https://api.scryfall.com/cards/search?order=set&q=e%3Am20&unique=prints"
        const val validRulingsUri = "https://api.scryfall.com/cards/m20/1/en?format=json&pretty=true"
        const val validScryfallUri = "https://scryfall.com/card/m20/1/elemental"
        const val validUri = "https://api.scryfall.com/cards/m20/1/en?format=json&pretty=true"

        val validAllParts = listOf(CommonRelatedCardObjectApi.elemental)
        val validCardFaces = listOf(CommonCardFaceApi.dragon)
        const val validCmc = 1.0
        val validColorIdentity = listOf("W", "U", "B", "R", "G")
        val validColorIndicator = listOf("W", "U", "B", "R", "G")
        val validColors = listOf("W", "U", "B", "R", "G")
        const val validEdhrecRank = 1
        const val validHandModifier = "W"
        val validKeywords = listOf("Flying")
        const val validLayout = "normal"
        val validLegalities = mapOf("standard" to "legal")
        const val validLifeModifier = "W"
        const val validLoyalty = "W"
        const val validManaCost = "{W}"
        const val validName = "Elemental"
        const val validOracleText = "Flying"
        const val validOversized = false
        const val validPennyRank = 1
        const val validPower = "1"
        val validProducedMana = listOf("W", "U", "B", "R", "G")
        const val validReserved = false
        const val validToughness = "1"
        const val validTypeLine = "Token Creature — Elemental"

        const val validArtist = "Artist"
        val validAttractionLights = listOf("Attraction Lights")
        const val validBooster = true
        const val validBorderColor = "border_color"
        const val validCardBackId = "card_back_id"
        const val validCollectorNumber = "collector_number"
        const val validContentWarning = true
        const val validDigital = true
        val validFinishes = listOf("finishes")
        const val validFlavorName = "flavor_name"
        const val validFlavorText = "flavor_text"
        val validFrameEffects = listOf("frame_effects")
        const val validFrame = "frame"
        const val validFullArt = true
        val validGames = listOf("games")
        const val validHighresImage = true
        const val validIllustrationId = "illustration_id"
        const val validImageStatus = "image_status"
        val validImageUris = CommonImageUrisApi.maskOfImmolation
        val validPrices = mapOf("prices" to "prices")
        const val validPrintedName = "printed_name"
        const val validPrintedText = "printed_text"
        const val validPrintedTypeLine = "printed_type_line"
        const val validPromo = true
        val validPromoTypes = listOf("promo_types")
        val validPurchaseUris = mapOf("purchase_uris" to "purchase_uris")
        const val validRarity = "rarity"
        val validRelatedUris = mapOf("related_uris" to "related_uris")
        const val validReleasedAt = "released_at"
        const val validReprint = true
        const val validSecurityStamp = "security_stamp"
        const val validScryfallSetUri = "scryfall_set_uri"
        const val validSetId = "set_id"
        const val validSetName = "set_name"
        const val validSetSearchUri = "set_search_uri"
        const val validSetType = "set_type"
        const val validSetUri = "set_uri"
        const val validSet = "set"
        const val validStorySpotlight = true
        const val validTextless = true
        const val validVariation = true
        const val validVariationOf = "variation_of"
        const val validWatermark = "watermark"
    }
}
