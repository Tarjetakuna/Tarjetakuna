package com.github.sdp.tarjetakuna.model

import com.github.sdp.tarjetakuna.ui.webapi.magicApi.MagicCard as MagicApiCard

class CardTransform {
    object CardTransform {
        fun cardFromWebApi(apiCard: MagicApiCard): MagicCard {
            return MagicCard(
                name = apiCard.name,
                text = apiCard.text,
                layout = magicLayoutFromWebApi(apiCard.layout),
                convertedManaCost = apiCard.cmc,
                manaCost = apiCard.manaCost,
                set = magicSetFromWebApi(apiCard),
                number = try {
                    apiCard.number.toInt()
                } catch (e: NumberFormatException) {
                    1
                },
                imageUrl = apiCard.imageUrl,
                rarity = magicRarityFromWebApi(apiCard.rarity),
                type = magicCardTypeFromWebApi(apiCard.type),
//                subtypes = ArrayList(apiCard.subtypes),
//                power = apiCard.power,
//                toughness = apiCard.toughness ?: "0",
//                artist = apiCard.artist,
            )
        }

        private fun magicLayoutFromWebApi(layout: String): MagicLayout {
            return when (layout) {
                "normal" -> MagicLayout.Normal
                "split" -> MagicLayout.Split
                "flip" -> MagicLayout.Flip
//                "transform" -> MagicLayout.Transform
//                "meld" -> MagicLayout.Meld
                "leveler" -> MagicLayout.Leveler
//                "saga" -> MagicLayout.Saga
//                "planar" -> MagicLayout.Planar
                "scheme" -> MagicLayout.Scheme
                "vanguard" -> MagicLayout.Vanguard
                "token" -> MagicLayout.Token
//                "double_faced_token" -> MagicLayout.DoubleFacedToken
//                "emblem" -> MagicLayout.Emblem
//                "augment" -> MagicLayout.Augment
//                "host" -> MagicLayout.Host
//                "art_series" -> MagicLayout.ArtSeries
//                "double_sided" -> MagicLayout.DoubleSided
                else -> MagicLayout.Normal
            }
        }

        private fun magicSetFromWebApi(apiCard: MagicApiCard): MagicSet {
            return MagicSet(
                name = apiCard.setName,
                code = apiCard.set,
                type = apiCard.type
            )
        }

        private fun magicRarityFromWebApi(rarity: String): MagicRarity {
            return when (rarity) {
                "common" -> MagicRarity.Common
                "uncommon" -> MagicRarity.Uncommon
                "rare" -> MagicRarity.Rare
                "mythic" -> MagicRarity.MythicRare
                else -> MagicRarity.Common
            }
        }

        private fun magicCardTypeFromWebApi(type: String): MagicCardType {
            return when (type) {
                "creature" -> MagicCardType.CREATURE
                "enchantment" -> MagicCardType.ENCHANTMENT
                "artifact" -> MagicCardType.ARTIFACT
                "instant" -> MagicCardType.INSTANT
                "sorcery" -> MagicCardType.SORCERY
                "land" -> MagicCardType.LAND
                "planeswalker" -> MagicCardType.PLANESWALKER
                "tribal" -> MagicCardType.TRIBAL
                else -> MagicCardType.CREATURE
            }
        }
    }
}
