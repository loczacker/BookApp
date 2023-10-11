package com.zacker.bookapp.ui.readbook

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ReadBookViewModel: ViewModel() {

    private var _nameChap = MutableLiveData<String>("")
    val nameChap: LiveData<String>
        get() = _nameChap

    fun setNameChap(newNameChap: String) {
        _nameChap.value = newNameChap
    }

    private var _content = MutableLiveData<String>("")
    val content: LiveData<String>
        get() = _content

    fun setContent(newContent: String) {
        _content.value = newContent
    }
}