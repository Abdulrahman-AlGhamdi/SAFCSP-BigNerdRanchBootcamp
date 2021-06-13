package com.ss.flickrlocation.Database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ss.flickrlocation.Model.Image.Photo

@Dao
interface FlickrDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAndUpdateFavorite(photo: Photo)

    @Delete
    suspend fun deleteFavorite(photo: Photo)

    @Query("SELECT * FROM photo_table ORDER BY id DESC")
    fun getAllFavorites(): LiveData<List<Photo>>

    @Query("DELETE FROM photo_table")
    suspend fun deleteAllFavorites()
}