package com.github.sdp.tarjetakuna.ui.webapi.magicApi

import com.github.sdp.tarjetakuna.utils.TestHelperWebApi
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class ForeignNameTest {

    @Test
    fun test_fields() {
        val card = TestHelperWebApi.getCardByIdResponse()
        card.foreignNames.forEach {
            assertThat("name not empty", it.name.isNotEmpty())
            assertThat("text not empty", it.text.isNotEmpty())
            assertThat("type not empty", it.type.isNotEmpty())
            assertThat("flavor value is null", it.flavor.toString(), equalTo("null"))
            assertThat("imageUrl not empty", it.imageUrl.isNotEmpty())
            assertThat("language not empty", it.language.isNotEmpty())
            assertThat("multiverseid not empty", it.multiverseid.toString().isNotEmpty())
        }
    }
}
