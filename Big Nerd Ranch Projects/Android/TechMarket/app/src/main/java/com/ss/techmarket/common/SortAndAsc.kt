package com.ss.techmarket.common

enum class Sort {
    TITLE {
        override fun value(): String = "title"
    },
    PRICE {
        override fun value(): String = "price"
    },
    CreatedAt {
        override fun value(): String = "created_at"
    },
    UpdatedAt {
        override fun value(): String = "updated_at"
    };

    abstract fun value(): String
}

enum class Asc {
    ASC {
        override fun value(): String = "asc"
    },
    DESC {
        override fun value(): String = "desc"
    };

    abstract fun value(): String
}