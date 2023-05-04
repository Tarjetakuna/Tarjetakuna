package com.github.sdp.tarjetakuna.ui.webapi.magicApi

import com.github.sdp.tarjetakuna.mockdata.CommonMagicApiCard
import junit.framework.TestCase.assertEquals
import org.junit.Test

class MagicApiCardTest {

    private val validArenaId = CommonMagicApiCard.MockCard.validArenaId
    private val validId = CommonMagicApiCard.MockCard.validId
    private val validLang = CommonMagicApiCard.MockCard.validLang
    private val validMtgoId = CommonMagicApiCard.MockCard.validMtgoId
    private val validMtgoFoilId = CommonMagicApiCard.MockCard.validMtgoFoilId
    private val validMultiverseIds = CommonMagicApiCard.MockCard.validMultiverseIds
    private val validTcgplayerId = CommonMagicApiCard.MockCard.validTcgplayerId
    private val validTcgplayerEtchedId = CommonMagicApiCard.MockCard.validTcgplayerEtchedId
    private val validCardmarketId = CommonMagicApiCard.MockCard.validCardmarketId
    private val validOracleId = CommonMagicApiCard.MockCard.validOracleId
    private val validPrintsSearchUri = CommonMagicApiCard.MockCard.validPrintsSearchUri
    private val validRulingsUri = CommonMagicApiCard.MockCard.validRulingsUri
    private val validScryfallUri = CommonMagicApiCard.MockCard.validScryfallUri
    private val validUri = CommonMagicApiCard.MockCard.validUri
    private val validAllParts = CommonMagicApiCard.MockCard.validAllParts
    private val validCardFaces = CommonMagicApiCard.MockCard.validCardFaces
    private val validCmc = CommonMagicApiCard.MockCard.validCmc
    private val validColorIdentity = CommonMagicApiCard.MockCard.validColorIdentity
    private val validColorIndicator = CommonMagicApiCard.MockCard.validColorIndicator
    private val validColors = CommonMagicApiCard.MockCard.validColors
    private val validEdhrecRank = CommonMagicApiCard.MockCard.validEdhrecRank
    private val validHandModifier = CommonMagicApiCard.MockCard.validHandModifier
    private val validLayout = CommonMagicApiCard.MockCard.validLayout
    private val validLegalities = CommonMagicApiCard.MockCard.validLegalities
    private val validLifeModifier = CommonMagicApiCard.MockCard.validLifeModifier
    private val validLoyalty = CommonMagicApiCard.MockCard.validLoyalty
    private val validManaCost = CommonMagicApiCard.MockCard.validManaCost
    private val validName = CommonMagicApiCard.MockCard.validName
    private val validOracleText = CommonMagicApiCard.MockCard.validOracleText
    private val validOversized = CommonMagicApiCard.MockCard.validOversized
    private val validPower = CommonMagicApiCard.MockCard.validPower
    private val validReserved = CommonMagicApiCard.MockCard.validReserved
    private val validToughness = CommonMagicApiCard.MockCard.validToughness
    private val validTypeLine = CommonMagicApiCard.MockCard.validTypeLine
    private val validArtist = CommonMagicApiCard.MockCard.validArtist
    private val validBorderColor = CommonMagicApiCard.MockCard.validBorderColor
    private val validCollectorNumber = CommonMagicApiCard.MockCard.validCollectorNumber
    private val validDigital = CommonMagicApiCard.MockCard.validDigital
    private val validFlavorText = CommonMagicApiCard.MockCard.validFlavorText
    private val validFrame = CommonMagicApiCard.MockCard.validFrame
    private val validFrameEffects = CommonMagicApiCard.MockCard.validFrameEffects
    private val validFullArt = CommonMagicApiCard.MockCard.validFullArt
    private val validGames = CommonMagicApiCard.MockCard.validGames
    private val validHighresImage = CommonMagicApiCard.MockCard.validHighresImage
    private val validIllustrationId = CommonMagicApiCard.MockCard.validIllustrationId
    private val validImageStatus = CommonMagicApiCard.MockCard.validImageStatus
    private val validPrices = CommonMagicApiCard.MockCard.validPrices
    private val validPrintedName = CommonMagicApiCard.MockCard.validPrintedName
    private val validPrintedText = CommonMagicApiCard.MockCard.validPrintedText
    private val validPrintedTypeLine = CommonMagicApiCard.MockCard.validPrintedTypeLine
    private val validPromo = CommonMagicApiCard.MockCard.validPromo
    private val validPromoTypes = CommonMagicApiCard.MockCard.validPromoTypes
    private val validPurchaseUris = CommonMagicApiCard.MockCard.validPurchaseUris
    private val validRarity = CommonMagicApiCard.MockCard.validRarity
    private val validRelatedUris = CommonMagicApiCard.MockCard.validRelatedUris
    private val validReleasedAt = CommonMagicApiCard.MockCard.validReleasedAt
    private val validReprint = CommonMagicApiCard.MockCard.validReprint
    private val validScryfallSetUri = CommonMagicApiCard.MockCard.validScryfallSetUri
    private val validSetId = CommonMagicApiCard.MockCard.validSetId
    private val validSetName = CommonMagicApiCard.MockCard.validSetName
    private val validSetSearchUri = CommonMagicApiCard.MockCard.validSetSearchUri
    private val validSetType = CommonMagicApiCard.MockCard.validSetType
    private val validSetUri = CommonMagicApiCard.MockCard.validSetUri
    private val validSet = CommonMagicApiCard.MockCard.validSet
    private val validKeywords = CommonMagicApiCard.MockCard.validKeywords
    private val validStorySpotlight = CommonMagicApiCard.MockCard.validStorySpotlight
    private val validWatermark = CommonMagicApiCard.MockCard.validWatermark
    private val validCardBackId = CommonMagicApiCard.MockCard.validCardBackId
    private val validPennyRank = CommonMagicApiCard.MockCard.validPennyRank
    private val validProducedMana = CommonMagicApiCard.MockCard.validProducedMana
    private val validAttractionLights = CommonMagicApiCard.MockCard.validAttractionLights
    private val validBooster = CommonMagicApiCard.MockCard.validBooster
    private val validContentWarning = CommonMagicApiCard.MockCard.validContentWarning
    private val validFinishes = CommonMagicApiCard.MockCard.validFinishes
    private val validFlavorName = CommonMagicApiCard.MockCard.validFlavorName
    private val validImageUris = CommonMagicApiCard.MockCard.validImageUris
    private val validSecurityStamp = CommonMagicApiCard.MockCard.validSecurityStamp
    private val validVariation = CommonMagicApiCard.MockCard.validVariation
    private val validTextless = CommonMagicApiCard.MockCard.validTextless
    private val validVariationOf = CommonMagicApiCard.MockCard.validVariationOf

    @Test
    fun validMagicApiCards() {
        val card = MagicApiCard(
            arena_id = validArenaId,
            id = validId,
            lang = validLang,
            mtgo_id = validMtgoId,
            mtgo_foil_id = validMtgoFoilId,
            multiverse_ids = validMultiverseIds,
            tcgplayer_id = validTcgplayerId,
            tcgplayer_etched_id = validTcgplayerEtchedId,
            cardmarket_id = validCardmarketId,
            oracle_id = validOracleId,
            prints_search_uri = validPrintsSearchUri,
            rulings_uri = validRulingsUri,
            scryfall_uri = validScryfallUri,
            uri = validUri,
            all_parts = validAllParts,
            card_faces = validCardFaces,
            cmc = validCmc,
            color_identity = validColorIdentity,
            color_indicator = validColorIndicator,
            colors = validColors,
            edhrec_rank = validEdhrecRank,
            hand_modifier = validHandModifier,
            keywords = validKeywords,
            layout = validLayout,
            legalities = validLegalities,
            life_modifier = validLifeModifier,
            loyalty = validLoyalty,
            mana_cost = validManaCost,
            name = validName,
            oracle_text = validOracleText,
            oversized = validOversized,
            penny_rank = validPennyRank,
            power = validPower,
            produced_mana = validProducedMana,
            reserved = validReserved,
            toughness = validToughness,
            type_line = validTypeLine,
            artist = validArtist,
            attraction_lights = validAttractionLights,
            booster = validBooster,
            border_color = validBorderColor,
            card_back_id = validCardBackId,
            collector_number = validCollectorNumber,
            content_warning = validContentWarning,
            digital = validDigital,
            finishes = validFinishes,
            flavor_name = validFlavorName,
            flavor_text = validFlavorText,
            frame_effects = validFrameEffects,
            frame = validFrame,
            full_art = validFullArt,
            games = validGames,
            highres_image = validHighresImage,
            illustration_id = validIllustrationId,
            image_status = validImageStatus,
            image_uris = validImageUris,
            prices = validPrices,
            printed_name = validPrintedName,
            printed_text = validPrintedText,
            printed_type_line = validPrintedTypeLine,
            promo = validPromo,
            promo_types = validPromoTypes,
            purchase_uris = validPurchaseUris,
            rarity = validRarity,
            related_uris = validRelatedUris,
            released_at = validReleasedAt,
            reprint = validReprint,
            security_stamp = validSecurityStamp,
            scryfall_set_uri = validScryfallSetUri,
            set_id = validSetId,
            set_name = validSetName,
            set_search_uri = validSetSearchUri,
            set_type = validSetType,
            set_uri = validSetUri,
            set = validSet,
            story_spotlight = validStorySpotlight,
            textless = validTextless,
            variation = validVariation,
            variation_of = validVariationOf,
            watermark = validWatermark,
        )

        assertEquals(validArenaId, card.arena_id)
        assertEquals(validId, card.id)
        assertEquals(validLang, card.lang)
        assertEquals(validMtgoId, card.mtgo_id)
        assertEquals(validMtgoFoilId, card.mtgo_foil_id)
        assertEquals(validMultiverseIds, card.multiverse_ids)
        assertEquals(validTcgplayerId, card.tcgplayer_id)
        assertEquals(validTcgplayerEtchedId, card.tcgplayer_etched_id)
        assertEquals(validCardmarketId, card.cardmarket_id)
        assertEquals(validOracleId, card.oracle_id)
        assertEquals(validPrintsSearchUri, card.prints_search_uri)
        assertEquals(validRulingsUri, card.rulings_uri)
        assertEquals(validScryfallUri, card.scryfall_uri)
        assertEquals(validUri, card.uri)
        assertEquals(validAllParts, card.all_parts)
        assertEquals(validCardFaces, card.card_faces)
        assertEquals(validCmc, card.cmc)
        assertEquals(validColorIdentity, card.color_identity)
        assertEquals(validColorIndicator, card.color_indicator)
        assertEquals(validColors, card.colors)
        assertEquals(validEdhrecRank, card.edhrec_rank)
        assertEquals(validHandModifier, card.hand_modifier)
        assertEquals(validKeywords, card.keywords)
        assertEquals(validLayout, card.layout)
        assertEquals(validLegalities, card.legalities)
        assertEquals(validLifeModifier, card.life_modifier)
        assertEquals(validLoyalty, card.loyalty)
        assertEquals(validManaCost, card.mana_cost)
        assertEquals(validName, card.name)
        assertEquals(validOracleText, card.oracle_text)
        assertEquals(validOversized, card.oversized)
        assertEquals(validPennyRank, card.penny_rank)
        assertEquals(validPower, card.power)
        assertEquals(validProducedMana, card.produced_mana)
        assertEquals(validReserved, card.reserved)
        assertEquals(validToughness, card.toughness)
        assertEquals(validTypeLine, card.type_line)
        assertEquals(validArtist, card.artist)
        assertEquals(validAttractionLights, card.attraction_lights)
        assertEquals(validBooster, card.booster)
        assertEquals(validBorderColor, card.border_color)
        assertEquals(validCardBackId, card.card_back_id)
        assertEquals(validCollectorNumber, card.collector_number)
        assertEquals(validContentWarning, card.content_warning)
        assertEquals(validDigital, card.digital)
        assertEquals(validFinishes, card.finishes)
        assertEquals(validFlavorName, card.flavor_name)
        assertEquals(validFlavorText, card.flavor_text)
        assertEquals(validFrameEffects, card.frame_effects)
        assertEquals(validFrame, card.frame)
        assertEquals(validFullArt, card.full_art)
        assertEquals(validGames, card.games)
        assertEquals(validHighresImage, card.highres_image)
        assertEquals(validIllustrationId, card.illustration_id)
        assertEquals(validImageStatus, card.image_status)
        assertEquals(validImageUris, card.image_uris)
        assertEquals(validPrices, card.prices)
        assertEquals(validPrintedName, card.printed_name)
        assertEquals(validPrintedText, card.printed_text)
        assertEquals(validPrintedTypeLine, card.printed_type_line)
        assertEquals(validPromo, card.promo)
        assertEquals(validPromoTypes, card.promo_types)
        assertEquals(validPurchaseUris, card.purchase_uris)
        assertEquals(validRarity, card.rarity)
        assertEquals(validRelatedUris, card.related_uris)
        assertEquals(validReleasedAt, card.released_at)
        assertEquals(validReprint, card.reprint)
        assertEquals(validSecurityStamp, card.security_stamp)
        assertEquals(validScryfallSetUri, card.scryfall_set_uri)
        assertEquals(validSetId, card.set_id)
        assertEquals(validSetName, card.set_name)
        assertEquals(validSetSearchUri, card.set_search_uri)
        assertEquals(validSetType, card.set_type)
        assertEquals(validSetUri, card.set_uri)
        assertEquals(validSet, card.set)
        assertEquals(validStorySpotlight, card.story_spotlight)
        assertEquals(validTextless, card.textless)
        assertEquals(validVariation, card.variation)
        assertEquals(validVariationOf, card.variation_of)
        assertEquals(validWatermark, card.watermark)
    }
}
