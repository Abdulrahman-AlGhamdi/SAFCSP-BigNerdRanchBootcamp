package com.ss.restaloca.recentSearch

import com.ss.restaloca.common.Businesse
import com.ss.restaloca.database.RestaLocaDatabase

class RecentSearchRepository(private var database: RestaLocaDatabase) {

    suspend fun addRecentSearch(business: Businesse) =
        database.recentSearchDao().addRecentSearch(business)

    suspend fun deleteAllRecentSearch() =
        database.recentSearchDao().deleteAllRecentSearch()

    fun getAllRecentSearchById() =
        database.recentSearchDao().getAllRecentSearchById()

    fun getAllRecentSearchByRating() =
        database.recentSearchDao().getAllRecentSearchByRating()

    fun getAllRecentSearchByPrice() =
        database.recentSearchDao().getAllRecentSearchByPrice()

    fun getAllRecentSearchReviewCount() =
        database.recentSearchDao().getAllRecentSearchByReviewCount()
}