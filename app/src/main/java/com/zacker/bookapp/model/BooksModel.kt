package com.zacker.bookapp.model

import java.io.Serializable

data class BooksModel(
    var nameBook: String ?= null,
    var writerName: String? = null,
    var img: String ?= null,
    var introduction: String? = null,
    var category: String ?= null,
    var timeStamp: Long ?= null,
    var chapter: ChapsModel ?= null
) : Serializable
