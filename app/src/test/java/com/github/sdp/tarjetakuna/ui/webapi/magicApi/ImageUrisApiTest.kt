package com.github.sdp.tarjetakuna.ui.webapi.magicApi

import com.github.sdp.tarjetakuna.mockdata.CommonImageUrisApi
import junit.framework.TestCase.assertEquals
import org.junit.Test

class ImageUrisApiTest {

    private val validArtCrop = CommonImageUrisApi.MaskOfImmolation.art_crop
    private val validBorderCrop = CommonImageUrisApi.MaskOfImmolation.border_crop
    private val validLarge = CommonImageUrisApi.MaskOfImmolation.large
    private val validNormal = CommonImageUrisApi.MaskOfImmolation.normal
    private val validPng = CommonImageUrisApi.MaskOfImmolation.png
    private val validSmall = CommonImageUrisApi.MaskOfImmolation.small

    @Test
    fun validImageUrisApi() {
        val imageUrisApi = ImageUrisApi(
            art_crop = validArtCrop,
            border_crop = validBorderCrop,
            large = validLarge,
            normal = validNormal,
            png = validPng,
            small = validSmall
        )

        assertEquals(imageUrisApi.art_crop, validArtCrop)
        assertEquals(imageUrisApi.border_crop, validBorderCrop)
        assertEquals(imageUrisApi.large, validLarge)
        assertEquals(imageUrisApi.normal, validNormal)
        assertEquals(imageUrisApi.png, validPng)
        assertEquals(imageUrisApi.small, validSmall)
    }
}
