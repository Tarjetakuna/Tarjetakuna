package com.github.sdp.tarjetakuna.extra

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Environment
import android.os.StrictMode
import android.view.View
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.GrantPermissionRule
import com.github.sdp.tarjetakuna.MainActivity
import com.github.sdp.tarjetakuna.R
import com.github.sdp.tarjetakuna.mockdata.CommonMagicCard
import com.github.sdp.tarjetakuna.utils.Utils
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.junit.*
import org.junit.Assert.*
import org.junit.runner.RunWith
import java.io.FileInputStream

/**
 * This class is used to test the export collection function
 */
@RunWith(AndroidJUnit4::class)
class ExportCollectionTest {

    @Rule
    @JvmField
    val grantPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private lateinit var view: View
    private lateinit var activityRule: ActivityScenario<MainActivity>

    private val validCollection = CommonMagicCard.validListOfCards

    @Before
    fun setUp() {
        Utils.useFirebaseEmulator()

        Intents.init()

        activityRule = ActivityScenario.launch(MainActivity::class.java)
        activityRule.onActivity {
            view = it.findViewById(R.id.nav_home)
        }
    }

    @After
    fun after() {
        activityRule.onActivity {
            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder().permitAll().build()
            )
        }
        Intents.release()
    }

    /**
     * This test checks that the function show a snackbar when the file path is not found
     */
    @Test
    fun filePathProblemShouldShowSnackBar() {
        activityRule.onActivity {
            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder().detectDiskReads().penaltyDeath().build()
            )
        }
        openActionBarOverflowOrOptionsMenu(getInstrumentation().targetContext)
        onView(withText(R.string.menu_exportcollection)).perform(click())

        onView(withText(R.string.ExportCollection_fileDirectoryNotFound))
            .check(matches(isDisplayed()))
    }

    /**
     * This test checks that the function send an intent with the action send
     */
    @Test
    fun clickExportButtonSendAnIntentWithActionSend() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().targetContext)
        onView(withText(R.string.menu_exportcollection)).perform(click())
        intended(hasAction(Intent.ACTION_SEND))
    }

    /**
     * This test checks that the function show a snackbar when the file can't be created
     */
    @Test
    fun fileProblemShowSnackBar() {
        activityRule.onActivity {
            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder().detectDiskWrites().penaltyDeath().build()
            )
        }
        openActionBarOverflowOrOptionsMenu(getInstrumentation().targetContext)
        onView(withText(R.string.menu_exportcollection)).perform(click())

        onView(withText(R.string.ExportCollection_fileCreationFailed))
            .check(matches(isDisplayed()))
    }

    /**
     * This test checks that the function show a snackbar when the intent fails
     */
    @Test
    fun intentFailureShowSnackBar() {
        Intents.intending(hasAction(Intent.ACTION_SEND)).respondWith(null)

        openActionBarOverflowOrOptionsMenu(getInstrumentation().targetContext)
        onView(withText(R.string.menu_exportcollection)).perform(click())

        onView(withText(R.string.ExportCollection_appNotFound))
            .check(matches(isDisplayed()))
    }

    /**
     * This test checks that the function write the right data into the excel file
     */
    @Test
    fun testRightDataAreWrittenIntoExcelFile() {
        ExportCollection.exportCollection(view, validCollection)

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

        for (i in validCollection.indices) {
            val dataRow = sheet.getRow(i + 1)
            assertNotNull(dataRow)
            val card = validCollection[i]
            assertEquals(card.name, dataRow.getCell(0).stringCellValue)
            assertEquals(card.text, dataRow.getCell(1).stringCellValue)
            assertEquals(card.manaCost, dataRow.getCell(2).stringCellValue)
            assertEquals(card.set.name, dataRow.getCell(3).stringCellValue)
            assertEquals(card.imageUrl, dataRow.getCell(4).stringCellValue)
        }
    }
}
