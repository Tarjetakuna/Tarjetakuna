package com.github.sdp.tarjetakuna.mockdata

import com.github.sdp.tarjetakuna.model.MagicSet
import java.time.LocalDate

/**
 * This is a common class that can be used to get [MagicSet] objects
 */
object CommonMagicSet {

    /**
     * This is a valid [MagicSet] Magic 2015
     */
    val magic2015Set: MagicSet = MagicSet("MT15", "Magic 2015", "core", "Core Set", LocalDate.parse("2014-07-18"))

    /**
     * This is a valid [MagicSet] Theros Beyond Death
     */
    val therosBeyondDeathSet: MagicSet = MagicSet("THB", "Theros Beyond Death", "expansion", "Theros Beyond Death", LocalDate.parse("2020-01-24"))

}
