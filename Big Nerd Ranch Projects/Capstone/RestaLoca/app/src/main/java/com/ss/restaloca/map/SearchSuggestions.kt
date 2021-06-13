package com.ss.restaloca.map

import android.content.SearchRecentSuggestionsProvider

class SearchSuggestions : SearchRecentSuggestionsProvider() {

    init {
        setupSuggestions(AUTHORITY, MODE)
    }

    companion object {
        const val AUTHORITY = "com.ss.restaloca.map.SearchSuggestions"
        const val MODE: Int = DATABASE_MODE_QUERIES
    }
}