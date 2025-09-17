package com.saefulrdevs.imagesconverter.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PDFViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PDFViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PDFViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
