package com.zacker.bookapp.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.zacker.bookapp.model.UsersModel
import com.zacker.jetpack.util.Event

class RegisterViewModel: ViewModel() {
    val resultData = MutableLiveData(false)

    private val _showToast = MutableLiveData<Event<Boolean>>()
    val showToast: LiveData<Event<Boolean>>
        get() = _showToast

    private val _showToast1 = MutableLiveData<Event<Boolean>>()
    val showToast1: LiveData<Event<Boolean>>
        get() = _showToast1

    fun register(email: String, pass: String, userName: String) {
        val auth = Firebase.auth
        val database = FirebaseDatabase.getInstance()
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Đăng ký thành công
                    val uid = auth.currentUser!!.uid
                    val databaseRef = database.reference.child("Users").child(uid)
                    val users = UsersModel("", email, "", userName, "", uid, "User")
                    databaseRef.setValue(users).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            login(email, pass)
                            showToastSuccessful()
                        } else {
                            showToastUnsuccessful()
                        }
                    }
                } else {
                    // Xử lý lỗi khi đăng ký không thành công
                    showToastUnsuccessful()
                }
            }
            .addOnFailureListener { e ->
                // Xử lý lỗi xảy ra trong quá trình đăng ký
                showToastUnsuccessful()
            }

    }


    private fun login(email: String, pass: String) {
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

    private fun showToastSuccessful() {
        _showToast.postValue(Event(true))
    }
    private fun showToastUnsuccessful() {
        _showToast1.postValue(Event(true))
    }

}