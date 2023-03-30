package com.github.sdp.tarjetakuna.ui.collectionexport


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import com.github.sdp.tarjetakuna.R
import com.github.sdp.tarjetakuna.model.MagicCard
import com.github.sdp.tarjetakuna.model.MagicLayout
import com.github.sdp.tarjetakuna.model.MagicSet
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.util.CellUtil
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * Object containing the methods to export the collection to an excel file
 */
object ExportCollection {

    private const val fileProviderAuthority = "com.github.sdp.tarjetakuna.fileprovider"

    // This is a test collection, it will be replaced later by the user's collection
    private val testCollection = listOf(
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

    /**
     * Export the collection of the user to an excel file
     * (The function is using a test collection for now, it will be changed later)
     */
    fun exportCollection(context: Context) {
        val fileName = "MyMagicCollection.xlsx"
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
                R.string.ExportCollection_fileCreationFailed,
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
        intent.type = "application/vnd.ms-excel"
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
