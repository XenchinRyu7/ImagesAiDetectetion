package com.saefulrdevs.imagesconverter.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pdfs")
data class PDF(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val dateCreated: Long,
    val size: Long,
)
