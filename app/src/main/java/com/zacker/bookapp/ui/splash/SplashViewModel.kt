package com.zacker.bookapp.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SplashViewModel: ViewModel() {
    private var _checkUser = MutableLiveData<Int>()
    val checkUser: LiveData<Int>
        get() = _checkUser

    val OPEN_LOGIN: Int = 1
    val OPEN_HOME: Int = 2
    val OPEN_ADMIN: Int = 3

    fun checkLogin(){
        if (FirebaseAuth.getInstance().currentUser == null) {
            _checkUser.postValue(OPEN_LOGIN)
        } else {
            _checkUser.postValue(OPEN_HOME)
//            checkAdmin()
        }
    }

//    private fun checkAdmin() {
//        val firebaseAuth = FirebaseAuth.getInstance()
//        val uid = firebaseAuth.uid.toString()
//        val database = FirebaseDatabase.getInstance()
//                database.reference.child("Users").child(uid)
//                    .addValueEventListener(object : ValueEventListener {
//                        override fun onDataChange(snapshot: DataSnapshot) {
//                            val presence = snapshot.child("presence").getValue(String::class.java)
//                            if (presence == "User") {
//                                _checkUser.postValue(OPEN_HOME)
//                            } else {
//                                _checkUser.postValue(OPEN_ADMIN)
//                            }
//                        }
//
//                        override fun onCancelled(error: DatabaseError) {}
//                    })
//    }
}