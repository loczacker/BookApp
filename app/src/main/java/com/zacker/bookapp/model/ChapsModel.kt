package com.zacker.bookapp.model

import java.io.Serializable

data class ChapsModel(
    var chapter: String ?= null,
    var content: String? = null,
) : Serializable
