package com.github.sdp.tarjetakuna.extra

import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import com.github.sdp.tarjetakuna.R
import com.github.sdp.tarjetakuna.model.MagicCard
import com.google.android.material.snackbar.Snackbar
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.util.CellUtil
import java.io.File
import java.io.FileOutputStream

/**
 * This class is used to export the user's collection to an excel file
 */
object ExportCollection {

    private const val fileProviderAuthority = "com.github.sdp.tarjetakuna.fileprovider"
    private const val fileName = "MyMagicCollection.xls"
    private const val sheetName = "MyMagicCollection"
    private const val applicationType = "application/excel"

    /**
     * Export the user's collection to an excel file
     */
    fun exportCollection(view: View, collectionToExport: List<MagicCard>) {
        //TODO: Run this function in an another thread to avoid blocking the UI + change tests accordingly
        val filePath: String
        try {
            filePath =
                view.context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)!!.absolutePath + "/" + fileName
        } catch (e: Exception) {
            Snackbar.make(
                view,
                R.string.ExportCollection_fileDirectoryNotFound,
                Snackbar.LENGTH_LONG
            ).show()
            return
        }

        val excelFile = File(filePath)
        if (writeDataToExcel(view, excelFile, collectionToExport)) shareExcelFile(
            view,
            excelFile
        )
    }

    /**
     * Write the collection to the excel file
     */
    private fun writeDataToExcel(
        view: View,
        excelFile: File,
        collectionToExport: List<MagicCard>
    ): Boolean {
        try {
            val workbook: Workbook = HSSFWorkbook()
            val sheet: Sheet = workbook.createSheet(sheetName)

            // Create header row
            val headerRow: Row = sheet.createRow(0)
            CellUtil.createCell(headerRow, 0, "Name")
            CellUtil.createCell(headerRow, 1, "Description")
            CellUtil.createCell(headerRow, 2, "Mana Cost")
            CellUtil.createCell(headerRow, 3, "Set")
            CellUtil.createCell(headerRow, 4, "Image URL")

            // Create data rows
            for (i in collectionToExport.indices) {
                val dataRow: Row = sheet.createRow(i + 1)
                CellUtil.createCell(dataRow, 0, collectionToExport[i].name)
                CellUtil.createCell(dataRow, 1, collectionToExport[i].text)
                CellUtil.createCell(dataRow, 2, collectionToExport[i].manaCost)
                CellUtil.createCell(dataRow, 3, collectionToExport[i].set.name)
                CellUtil.createCell(dataRow, 4, collectionToExport[i].imageUrl)
            }

            // Save workbook to file
            val fileOutputStream = FileOutputStream(excelFile)
            workbook.write(fileOutputStream)
            fileOutputStream.close()
        } catch (e: Exception) {
            Snackbar.make(
                view,
                R.string.ExportCollection_fileCreationFailed,
                Snackbar.LENGTH_SHORT
            ).show()
            return false
        }
        return true
    }

    /**
     * Open the view to share the excel file
     */
    private fun shareExcelFile(view: View, excelFile: File) {
        val uri: Uri =
            FileProvider.getUriForFile(view.context, fileProviderAuthority, excelFile)

        val intent = Intent(Intent.ACTION_SEND)
        intent.type = applicationType
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        try {
            startActivity(view.context, intent, null)
        } catch (e: Exception) {
            Snackbar.make(
                view,
                R.string.ExportCollection_appNotFound,
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }
}
