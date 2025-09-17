package com.saefulrdevs.imagesconverter.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.saefulrdevs.imagesconverter.data.model.PDF

@Database(entities = [PDF::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pdfDao(): PDFDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        private var DB_NAME: String = "pdf_database.db"

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DB_NAME
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
