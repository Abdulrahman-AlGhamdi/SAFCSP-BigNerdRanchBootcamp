package com.ss.restaloca.recentSearch

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ss.restaloca.common.Businesse

@Dao
interface RecentSearchDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRecentSearch(business: Businesse)

    @Query("DELETE FROM recent_search_table")
    suspend fun deleteAllRecentSearch()

    @Query("SELECT * FROM recent_search_table ORDER BY id DESC")
    fun getAllRecentSearchById(): LiveData<List<Businesse>>

    @Query("SELECT * FROM recent_search_table ORDER BY rating DESC")
    fun getAllRecentSearchByRating(): LiveData<List<Businesse>>

    @Query("SELECT * FROM recent_search_table ORDER BY price DESC")
    fun getAllRecentSearchByPrice(): LiveData<List<Businesse>>

    @Query("SELECT * FROM recent_search_table ORDER BY review_count DESC")
    fun getAllRecentSearchByReviewCount(): LiveData<List<Businesse>>
}