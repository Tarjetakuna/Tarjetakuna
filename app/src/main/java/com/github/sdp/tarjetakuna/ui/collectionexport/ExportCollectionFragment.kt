package com.github.sdp.tarjetakuna.ui.collectionexport

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.github.sdp.tarjetakuna.databinding.FragmentExportCollectionBinding
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
 * This fragment is responsible to create an excel file containing the user's collection,
 * permitting the user to export it
 */
class ExportCollectionFragment : Fragment() {

    private lateinit var binding: FragmentExportCollectionBinding
    private val fileProviderAuthority = "com.github.sdp.tarjetakuna.fileprovider"

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExportCollectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.exportCollectionButton.setOnClickListener {
            // Create a new excel file
            val fileName = "MyMagicCollection.xls"
            val filePath =
                requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)?.absolutePath + "/" + fileName
            val excelFile = File(filePath)

            // Write the collection to the excel file (to be changed later, when the user's collection is available)
            writeDataToExcel(excelFile, testCollection)

            // Open the view to share the excel file
            openExcelFile(excelFile)
        }
    }

    /**
     * Write the collection to the excel file
     */
    private fun writeDataToExcel(excelFile: File, collection: List<MagicCard>) {
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
                this.requireContext(),
                "Failed to write data to Excel file",
                Toast.LENGTH_SHORT
            ).show()
            e.printStackTrace()
        }
    }

    /**
     * Open the view to share the excel file
     */
    private fun openExcelFile(excelFile: File) {
        val uri: Uri =
            FileProvider.getUriForFile(this.requireContext(), fileProviderAuthority, excelFile)

        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "application/excel"
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        try {
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(
                this.requireContext(),
                "No app found to share Excel file",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
