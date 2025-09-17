package com.saefulrdevs.imagesconverter.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.saefulrdevs.imagesconverter.data.database.PDFDao
import com.saefulrdevs.imagesconverter.data.model.PDF
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PDFRepository(private val pdfDao: PDFDao) {
    val allPDFs: LiveData<List<PDF>> = pdfDao.getAll().asLiveData()

    suspend fun insert(pdf: PDF) {
        withContext(Dispatchers.IO) {
            Log.d("PDFRepository", "Inserting PDF: ${pdf.name}, Size: ${pdf.size}, Date Created: ${pdf.dateCreated}")
            pdfDao.insert(pdf)
        }
    }

    suspend fun delete(pdf: PDF) {
        withContext(Dispatchers.IO) {
            Log.d("PDFRepository", "Deleting PDF: ${pdf.name}")
            pdfDao.delete(pdf)
        }
    }
}


