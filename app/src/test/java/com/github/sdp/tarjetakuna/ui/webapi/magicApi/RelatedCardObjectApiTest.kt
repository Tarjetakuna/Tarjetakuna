package com.github.sdp.tarjetakuna.ui.webapi.magicApi

import com.github.sdp.tarjetakuna.mockdata.CommonRelatedCardObjectApi
import junit.framework.TestCase.assertEquals
import org.junit.Test

class RelatedCardObjectApiTest {

    private val validId = CommonRelatedCardObjectApi.Elemental.id
    private val validComponent = CommonRelatedCardObjectApi.Elemental.component
    private val validName = CommonRelatedCardObjectApi.Elemental.name
    private val validTypeLine = CommonRelatedCardObjectApi.Elemental.type_line
    private val validUri = CommonRelatedCardObjectApi.Elemental.uri

    @Test
    fun validRelatedCardObjectApi() {
        val relatedCardObjectApi = RelatedCardObjectApi(
            validId,
            validComponent,
            validName,
            validTypeLine,
            validUri
        )

        assertEquals(relatedCardObjectApi.id, validId)
        assertEquals(relatedCardObjectApi.component, validComponent)
        assertEquals(relatedCardObjectApi.name, validName)
        assertEquals(relatedCardObjectApi.type_line, validTypeLine)
        assertEquals(relatedCardObjectApi.uri, validUri)
    }
}
