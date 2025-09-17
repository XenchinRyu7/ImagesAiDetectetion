package com.saefulrdevs.imagesconverter.ui.screens.textrecognitionscreen

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel : ViewModel() {

    private val _bitmaps = MutableStateFlow<List<Bitmap>>(emptyList())
    val bitmaps = _bitmaps.asStateFlow()

    fun onTakePhoto(bitmap: Bitmap) {
        _bitmaps.value += bitmap
    }
    
    fun clearAllPhotos() {
        _bitmaps.value = emptyList()
    }
    
    fun removePhoto(index: Int) {
        if (index in _bitmaps.value.indices) {
            _bitmaps.value = _bitmaps.value.toMutableList().apply { removeAt(index) }
        }
    }
}