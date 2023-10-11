package com.zacker.bookapp.ui.homeDiscover

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zacker.bookapp.model.BooksModel

class HomeDiscoverViewModel: ViewModel() {
    val resultBookSort = MutableLiveData<List<BooksModel>>()

}