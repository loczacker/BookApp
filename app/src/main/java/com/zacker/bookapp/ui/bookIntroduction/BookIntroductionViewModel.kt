package com.zacker.bookapp.ui.bookIntroduction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BookIntroductionViewModel: ViewModel() {


        private var _nameBook = MutableLiveData<String>("")
        val nameBook: LiveData<String>
            get() = _nameBook

        fun setNameBook(newNameBook: String) {
            _nameBook.value = newNameBook
        }

}