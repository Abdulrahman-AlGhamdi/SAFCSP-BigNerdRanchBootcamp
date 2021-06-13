package com.ss.restaloca.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ss.restaloca.common.Businesse
import com.ss.restaloca.common.Constants.DATABASE_NAME
import com.ss.restaloca.reservation.ReservationModel
import com.ss.restaloca.recentSearch.RecentSearchDao
import com.ss.restaloca.reservation.ReservationDao

@Database(entities = [Businesse::class, ReservationModel::class], version = 1)
@TypeConverters(RestaLocaConverters::class)
abstract class RestaLocaDatabase : RoomDatabase() {

    abstract fun reservationDao(): ReservationDao
    abstract fun recentSearchDao(): RecentSearchDao

    companion object {

        @Volatile
        private var INSTANCE: RestaLocaDatabase? = null
        private val lock = Any()

        operator fun invoke(context: Context) = INSTANCE ?: synchronized(lock) {
            INSTANCE ?: createDatabase(context).also { INSTANCE = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                RestaLocaDatabase::class.java,
                DATABASE_NAME
            ).build()
    }
}