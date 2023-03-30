package com.github.sdp.tarjetakuna.ui.browser

import androidx.lifecycle.ViewModel
import com.github.sdp.tarjetakuna.model.*

class BrowserViewModel : ViewModel() {

    val initialCards: ArrayList<MagicCard> = generateCards()

    /**
     * TODO change it when we have the web api to get the cards
     * Generate cards in order to simulate the display of the cards
     */
    private fun generateCards(): ArrayList<MagicCard> {
        val cardsArray = ArrayList<MagicCard>()

        cardsArray.add(
            MagicCard(
                "Gwenna, Eyes of Gaea",
                "<-: Add two mana in any combination of colors. Spend this mana only to cast creature spells or activate abilities of a creature or creature card.\n" +
                        "Whenever you cast a creature spell with power 5 or greater, put a +1/+1 counter on Gwenna, Eyes of Gaea and untap it.",
                MagicLayout.Normal,
                3,
                "{2}{W}",
                MagicSet("BRO", "The Brothers War"),
                185,
                "https://cards.scryfall.io/large/front/7/e/7ee387b7-18e4-41b7-aefe-f2b5954e3051.jpg?1674421589",
                MagicRarity.Rare,
                MagicType.Creature,
                listOf("Elf", "Druid", "Scout"),
                "2",
                "3",
                "Steve Prescott"
            )
        )
        cardsArray.add(
            MagicCard(
                "Calamity's Wake",
                "Exile all graveyards. Players can’t cast noncreature spells this turn. Exile Calamity’s Wake.",
                MagicLayout.Normal,
                2,
                "{1}{W}",
                MagicSet("BRO", "The Brothers War"),
                4,
                "https://cards.scryfall.io/large/front/0/1/013bed2b-25db-4dfc-9283-e80c9ac6c841.jpg?1675567420",
                MagicRarity.Uncommon,
                MagicType.Instant,
                listOf(),
                "0",
                "0",
                "Slawomir Maniak"
            )
        )
        cardsArray.add(
            MagicCard(
                "Moutain",
                "<-: Add {R} to your mana pool.",
                MagicLayout.Normal,
                0,
                "0",
                MagicSet("BRO", "The Brothers War"),
                285,
                "https://cards.scryfall.io/large/front/c/d/cd757142-45b7-40db-80f2-fe161379aa4e.jpg?1674422365",
                MagicRarity.Common,
                MagicType.Land,
                listOf(),
                "0",
                "0",
                "Victor Harmatiuk"
            )
        )
        cardsArray.add(
            MagicCard(
                "Raze to the Ground",
                "This spell can’t be countered." +
                        "\n" +
                        "Destroy target artifact. If its mana value was 1 or less, draw a card.",
                MagicLayout.Normal,
                3,
                "{2}{R}",
                MagicSet("BRO", "The Brothers War"),
                149,
                "https://cards.scryfall.io/large/front/8/3/838b25d2-7615-4375-a5e6-3d762c9072a5.jpg?1674421245",
                MagicRarity.Common,
                MagicType.Sorcery,
                listOf(),
                "0",
                "0",
                "Joshua Raphael"
            )
        )
        cardsArray.add(
            MagicCard(
                "Disciples of Gix",
                "When Disciples of Gix enters the battlefield, search your library for up to three artifact cards, put them into your graveyard, then shuffle.",
                MagicLayout.Normal,
                6,
                "{4}{B}{B}",
                MagicSet("BRO", "The Brothers War"),
                90,
                "https://cards.scryfall.io/large/front/7/d/7d356456-865d-4c92-8923-ce7384e29a79.jpg?1674420807",
                MagicRarity.Common,
                MagicType.Creature,
                listOf("Phyrexian", "Human"),
                "4",
                "4",
                "Peter Polach"
            )
        )
        cardsArray.add(
            MagicCard(
                "Ashnod's Harvester",
                "Whenever Ashnod's Harvester attacks, exile target card from a graveyard.\nUnearth {1}{B}",
                MagicLayout.Normal,
                2,
                "{2}",
                MagicSet("BRO", "The Brothers War"),
                117,
                "https://cards.scryfall.io/large/front/5/8/58baa977-16c1-4983-8343-dbd65e98ddb7.jpg?1674421008",
                MagicRarity.Uncommon,
                MagicType.Creature,
                listOf("Construct"),
                "3",
                "1",
                "Halil Ural"
            )
        )
        cardsArray.add(
            MagicCard(
                "Haywire Mite",
                "When Haywire Mite dies, you gain 2 life.\n" +
                        "{G}, Sacrifice Haywire Mite: Exile target noncreature artifact or noncreature enchantment",
                MagicLayout.Normal,
                1,
                "{1}",
                MagicSet("BRO", "The Brothers War"),
                199,
                "https://cards.scryfall.io/large/front/8/4/847a175e-ead1-4596-baf3-5f7f57859e0b.jpg?1674421689",
                MagicRarity.Uncommon,
                MagicType.Creature,
                listOf("Insect"),
                "1",
                "1",
                "Izzy"
            )
        )
        cardsArray.add(
            MagicCard(
                "Mightstone's Animation",
                "Enchant artifact\n" +
                        "When Mightstone’s Animation enters the battlefield, draw a card.\n" +
                        "Enchanted artifact is a creature with base power and toughness 4/4 in addition to its other types.",
                MagicLayout.Normal,
                4,
                "{3}{B}",
                MagicSet("BRO", "The Brothers War"),
                58,
                "https://cards.scryfall.io/large/front/8/c/8c540e42-22e2-4127-bbd9-2e9200023fec.jpg?1674420567",
                MagicRarity.Common,
                MagicType.Enchantment,
                listOf("Aura"),
                "0",
                "0",
                "Igor Kieryluk"
            )
        )
        cardsArray.add(
            MagicCard(
                "Defabricate",
                "Choose one —\n" +
                        "• Counter target artifact or enchantment spell. If a spell is countered this way, exile it instead of putting it into its owner’s graveyard.\n" +
                        "• Counter target activated or triggered ability.",
                MagicLayout.Normal,
                2,
                "{1}{B}",
                MagicSet("BRO", "The Brothers War"),
                45,
                "https://cards.scryfall.io/large/front/7/d/7dd32168-0b9c-4633-adec-d41cf125cc35.jpg?1674420469",
                MagicRarity.Uncommon,
                MagicType.Instant,
                listOf(),
                "0",
                "0",
                "Uriah Voth"
            )
        )
        cardsArray.add(
            MagicCard(
                "Deadly Riposte",
                "Deadly Riposte deals 3 damage to target tapped creature and you gain 2 life.",
                MagicLayout.Normal,
                2,
                "{1}{W}",
                MagicSet("BRO", "The Brothers War"),
                5,
                "https://cards.scryfall.io/large/front/3/8/38eca0ae-d400-4afb-9a45-7100f4cd7149.jpg?1674420153",
                MagicRarity.Common,
                MagicType.Instant,
                listOf(),
                "0",
                "0",
                "Olena Richards"
            )
        )
        return cardsArray
    }
}
