package com.github.sdp.tarjetakuna.ui.collectionexport


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import com.github.sdp.tarjetakuna.R
import com.github.sdp.tarjetakuna.model.*
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.util.CellUtil
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object ExportCollection {

    private const val fileProviderAuthority = "com.github.sdp.tarjetakuna.fileprovider"

    // This is a test collection, it will be replaced later by the user's collection
    private val testCollection = listOf(
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
            ),
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
                ),
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
                ),
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
                ),
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
                ),
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
                ),
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
                ),
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
                ),
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
                ),
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

    fun exportCollection(context: Context) {
        val fileName = "MyMagicCollection.xls"
        val filePath =
            context
                .getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)?.absolutePath + "/" + fileName
        val excelFile = File(filePath)

        // Write the collection to the excel file (to be changed later, when the user's collection is available)
        writeDataToExcel(context, excelFile, testCollection)

        // Open the view to share the excel file
        openExcelFile(context, excelFile)
    }

    /**
     * Write the collection to the excel file
     */
    private fun writeDataToExcel(context: Context, excelFile: File, collection: List<MagicCard>) {
        try {
            val workbook: Workbook = HSSFWorkbook()
            val sheet: Sheet = workbook.createSheet("NewSheet")

            // Create header row
            val headerRow: Row = sheet.createRow(0)
            CellUtil.createCell(headerRow, 0, "Name")
            CellUtil.createCell(headerRow, 1, "Description")
            CellUtil.createCell(headerRow, 2, "Mana Cost")
            CellUtil.createCell(headerRow, 3, "Set")
            CellUtil.createCell(headerRow, 4, "Image URL")

            // Create data rows
            for (i in collection.indices) {
                val dataRow: Row = sheet.createRow(i + 1)
                CellUtil.createCell(dataRow, 0, collection[i].name)
                CellUtil.createCell(dataRow, 1, collection[i].text)
                CellUtil.createCell(dataRow, 2, collection[i].manaCost)
                CellUtil.createCell(dataRow, 3, collection[i].set.name)
                CellUtil.createCell(dataRow, 4, collection[i].imageUrl)
            }

            // Save workbook to file
            val fileOutputStream = FileOutputStream(excelFile)
            workbook.write(fileOutputStream)
            fileOutputStream.close()
        } catch (e: IOException) {
            Toast.makeText(
                context,
                "Failed to write data to Excel file",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    /**
     * Open the view to share the excel file
     */
    private fun openExcelFile(context: Context, excelFile: File) {
        val uri: Uri =
            FileProvider.getUriForFile(context, fileProviderAuthority, excelFile)

        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "application/excel"
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        try {
            startActivity(context, intent, null)
        } catch (e: Exception) {
            Toast.makeText(
                context,
                R.string.ExportCollection_appNotFound,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
