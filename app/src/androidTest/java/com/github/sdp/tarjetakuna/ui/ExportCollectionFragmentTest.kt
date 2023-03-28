package com.github.sdp.tarjetakuna.ui

import android.content.Context
import android.content.Intent
import android.os.Environment
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdp.tarjetakuna.R
import com.github.sdp.tarjetakuna.model.MagicCard
import com.github.sdp.tarjetakuna.model.MagicLayout
import com.github.sdp.tarjetakuna.model.MagicSet
import com.github.sdp.tarjetakuna.ui.collectionexport.ExportCollectionFragment
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.FileInputStream

@RunWith(AndroidJUnit4::class)
class ExportCollectionFragmentTest {

    private lateinit var scenario: FragmentScenario<ExportCollectionFragment>

    @Before
    fun setUp() {
        Intents.init()
        scenario = launchFragmentInContainer()
    }

    @After
    fun after() {
        Intents.release()
        scenario.close()
    }

    @Test
    fun testExportButtonNotNull() {
        assertNotNull(onView(withId(R.id.exportCollectionButton)))
    }

    @Test
    fun testExportButtonIsDisplayed() {
        onView(withId(R.id.exportCollectionButton)).check(matches(isDisplayed()))
    }

    @Test
    fun clickExportButtonSendAnIntentWithActionSend() {
        onView(withId(R.id.exportCollectionButton)).perform(click())
        intended(hasAction(Intent.ACTION_SEND))
    }

    @Test
    fun testRightDataAreWrittenIntoExcelFile() {
        //Unused value which will be usefull when the actual user collection will be used
        val testCollection = listOf(
            MagicCard(
                "MagicCard",
                "A beautiful card",
                MagicLayout.Normal,
                7,
                "{5}{W}{W}",
                MagicSet("MT15", "Magic 2015"),
                56,
                "https://img.scryfall.com/cards/large/front/1/2/12345678.jpg?1562567890"
            ),
            MagicCard(
                "BestMagicCard",
                "An even more beautiful card",
                MagicLayout.Normal,
                7,
                "{7}{W}{W}",
                MagicSet("MT15", "Magic 2015"),
                56,
                "https://img.scryfall.com/cards/large/front/1/2/12345678.jpg?1562567890"
            )
        )
        onView(withId(R.id.exportCollectionButton)).perform(click())

        val excelFile = ApplicationProvider.getApplicationContext<Context>()
            .getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)?.resolve("MyMagicCollection.xls")


        val workbook = HSSFWorkbook(FileInputStream(excelFile))
        val sheet = workbook.getSheetAt(0)

        val headerRow = sheet.getRow(0)
        assertNotNull(headerRow)
        assertEquals("Name", headerRow.getCell(0).stringCellValue)
        assertEquals("Description", headerRow.getCell(1).stringCellValue)
        assertEquals("Mana Cost", headerRow.getCell(2).stringCellValue)
        assertEquals("Set", headerRow.getCell(3).stringCellValue)
        assertEquals("Image URL", headerRow.getCell(4).stringCellValue)

        val dataRow1 = sheet.getRow(1)
        assertNotNull(dataRow1)
        assertEquals("MagicCard", dataRow1.getCell(0).stringCellValue)
        assertEquals("A beautiful card", dataRow1.getCell(1).stringCellValue)
        assertEquals("{5}{W}{W}", dataRow1.getCell(2).stringCellValue)
        assertEquals("Magic 2015", dataRow1.getCell(3).stringCellValue)
        assertEquals(
            "https://img.scryfall.com/cards/large/front/1/2/12345678.jpg?1562567890",
            dataRow1.getCell(4).stringCellValue
        )

        val dataRow2 = sheet.getRow(2)
        assertNotNull(dataRow2)
        assertEquals("BestMagicCard", dataRow2.getCell(0).stringCellValue)
        assertEquals("An even more beautiful card", dataRow2.getCell(1).stringCellValue)
        assertEquals("{7}{W}{W}", dataRow2.getCell(2).stringCellValue)
        assertEquals("Magic 2015", dataRow2.getCell(3).stringCellValue)
        assertEquals(
            "https://img.scryfall.com/cards/large/front/1/2/12345678.jpg?1562567890",
            dataRow2.getCell(4).stringCellValue
        )
    }
}