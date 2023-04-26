package com.github.sdp.tarjetakuna.utils

import com.github.sdp.tarjetakuna.model.MagicSet
import java.time.LocalDate

/**
 * This is a common class that can be used in both _androidTest_ and _unitTest_ to get [MagicSet] objects
 */
class CommonMagicSet {

    /**
     * This is a valid [MagicSet] object
     */
    val validSet: MagicSet = MagicSet("MT15", "Magic 2015", "core", "Core Set", LocalDate.parse("2014-07-18"))

}
