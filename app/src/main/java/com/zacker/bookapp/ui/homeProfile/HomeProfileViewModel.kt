package com.zacker.bookapp.ui.homeProfile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeProfileViewModel:ViewModel() {

    private var _email = MutableLiveData<String>()
    val email: LiveData<String>
        get() = _email

    private val _name = MutableLiveData<String>( )
    val name: LiveData<String>
        get() = _name

    private val _img = MutableLiveData<String>( )
    val img: LiveData<String>
        get() = _img


    fun showProfile() {
        val ref = FirebaseDatabase.getInstance().getReference("Users")
        val firebaseAuth = FirebaseAuth.getInstance()
        ref.child(firebaseAuth.uid!!).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                _email.value = "${snapshot.child("email").value}"
                _name.value = "${snapshot.child("name").value}"
                _img.value = "${snapshot.child("img").value}"
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }
    fun signOut(){
        FirebaseAuth.getInstance().signOut()
    }
}