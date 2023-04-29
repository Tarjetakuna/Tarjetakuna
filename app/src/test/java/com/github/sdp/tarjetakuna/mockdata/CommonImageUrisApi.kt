package com.github.sdp.tarjetakuna.mockdata

import com.github.sdp.tarjetakuna.ui.webapi.magicApi.ImageUrisApi

/**
 * Common [CommonImageUrisApi] to use in tests
 */
object CommonImageUrisApi {

    val maskOfImmolation = ImageUrisApi(
        art_crop = MaskOfImmolation.art_crop,
        border_crop = MaskOfImmolation.border_crop,
        large = MaskOfImmolation.large,
        normal = MaskOfImmolation.normal,
        png = MaskOfImmolation.png,
        small = MaskOfImmolation.small
    )

    object MaskOfImmolation {
        const val art_crop = "https://cards.scryfall.io/art_crop/front/b/e/be2f4165-f87f-454b-b955-e4477c864e95.jpg?1592517021"
        const val border_crop = "https://cards.scryfall.io/border_crop/front/b/e/be2f4165-f87f-454b-b955-e4477c864e95.jpg?1592517021"
        const val large = "https://cards.scryfall.io/large/front/b/e/be2f4165-f87f-454b-b955-e4477c864e95.jpg?1592517021"
        const val normal = "https://cards.scryfall.io/normal/front/b/e/be2f4165-f87f-454b-b955-e4477c864e95.jpg?1592517021"
        const val png = "https://cards.scryfall.io/png/front/b/e/be2f4165-f87f-454b-b955-e4477c864e95.png?1592517021"
        const val small = "https://cards.scryfall.io/small/front/b/e/be2f4165-f87f-454b-b955-e4477c864e95.jpg?1592517021"
    }
}
