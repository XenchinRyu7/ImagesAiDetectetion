package com.saefulrdevs.imagesconverter.data.database

import androidx.room.*
import com.saefulrdevs.imagesconverter.data.model.PDF
import kotlinx.coroutines.flow.Flow

@Dao
interface PDFDao {
    @Query("SELECT * FROM pdfs")
    fun getAll(): Flow<List<PDF>>

    @Insert
    fun insert(pdf: PDF)

    @Delete
    fun delete(pdf: PDF)
}
