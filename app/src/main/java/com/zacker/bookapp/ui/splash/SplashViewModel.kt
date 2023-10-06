package com.zacker.bookapp.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class SplashViewModel: ViewModel() {
    private var _checkUser = MutableLiveData<Int>()
    val checkUser: LiveData<Int>
        get() = _checkUser

    val OPEN_LOGIN: Int = 1
    val OPEN_HOME: Int = 2

    fun checkLogin(){
        if (FirebaseAuth.getInstance().currentUser == null) {
            _checkUser.postValue(OPEN_LOGIN)
        } else {
            _checkUser.postValue(OPEN_HOME)
        }
    }
}