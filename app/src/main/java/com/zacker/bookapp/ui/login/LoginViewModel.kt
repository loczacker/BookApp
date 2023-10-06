package com.zacker.bookapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.zacker.jetpack.util.Event
import com.zacker.jetpack.util.SingleLiveEvent

class LoginViewModel: ViewModel() {

    val resultData = MutableLiveData(false)

    private val _startRegister = SingleLiveEvent<Boolean>()
    val startRegister: SingleLiveEvent<Boolean>
        get() = _startRegister

    //One time event
    private val _showToast = MutableLiveData<Event<Boolean>>()
    val showToast: LiveData<Event<Boolean>>
        get() = _showToast

    fun showToast() {
        _showToast.value = Event(true) // run immediately => chay ngay lap tuc
        _showToast.postValue(Event(true)) // handler.post chay sau 1 chut nhung khong anh huong den performance
//        _showToast.postValue(true)
//        _showToast.postValue(false)
    }


    fun login(email: String, pass: String) {
        val auth: FirebaseAuth = Firebase.auth
        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    resultData.postValue(true)
                } else {
                    resultData.postValue(false)
                }
            }
    }

    fun showRegister(){
        _startRegister.value = true
    }

}