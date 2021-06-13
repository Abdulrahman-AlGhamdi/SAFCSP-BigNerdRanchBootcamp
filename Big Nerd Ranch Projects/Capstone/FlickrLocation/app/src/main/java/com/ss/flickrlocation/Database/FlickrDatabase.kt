package com.ss.flickrlocation.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ss.flickrlocation.Model.Image.Photo
import com.ss.flickrlocation.Utils.Constant.DATABASE_NAME

@Database(entities = [Photo::class], version = 1)
abstract class FlickrDatabase : RoomDatabase() {

    abstract fun flickrDao(): FlickrDao

    companion object {

        @Volatile
        private var INSTANCE: FlickrDatabase? = null
        private val lock = Any()

        operator fun invoke(context: Context) = INSTANCE ?: synchronized(lock) {
            INSTANCE ?: createDatabase(context).also { INSTANCE = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                FlickrDatabase::class.java,
                DATABASE_NAME
            ).build()
    }
}