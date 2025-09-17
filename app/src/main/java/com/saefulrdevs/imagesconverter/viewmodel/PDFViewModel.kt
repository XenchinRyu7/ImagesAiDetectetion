package com.saefulrdevs.imagesconverter.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.saefulrdevs.imagesconverter.data.database.AppDatabase
import com.saefulrdevs.imagesconverter.data.model.PDF
import com.saefulrdevs.imagesconverter.data.repository.PDFRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PDFViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PDFRepository
    val allPDFs: LiveData<List<PDF>>

    init {
        val pdfDao = AppDatabase.getDatabase(application).pdfDao()
        repository = PDFRepository(pdfDao)
        allPDFs = repository.allPDFs
    }

    fun insert(pdf: PDF) = viewModelScope.launch {
        repository.insert(pdf)
    }

    fun delete(pdf: PDF) = viewModelScope.launch {
        repository.delete(pdf)
    }
}
