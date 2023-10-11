package com.zacker.bookapp.ui.changeProfile

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.zacker.bookapp.model.UsersModel
import java.util.HashMap

class ChangeProfileViewModel: ViewModel() {
    val resultUser = MutableLiveData<UsersModel?>()
    fun setUpView() {
        val user = Firebase.auth.currentUser
        user?.let {
            val uid: String = user.uid
            FirebaseDatabase.getInstance().getReference("Constants.USERS")
                .child(uid)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val users: UsersModel? = dataSnapshot.getValue(UsersModel::class.java)
                        resultUser.postValue(users)
                    }
                    override fun onCancelled(databaseError: DatabaseError) {}
                })
        }

    }

    fun uploadImage(mUri: Uri) {
        val reference = FirebaseStorage.getInstance().reference
        val riversRef = reference.child("images/" + mUri.lastPathSegment)
        riversRef.putFile(mUri).addOnSuccessListener {
            riversRef.downloadUrl.addOnSuccessListener { uri ->
                val user = Firebase.auth.currentUser
                user?.let {
                    val uid: String = user.uid
                    FirebaseDatabase.getInstance().getReference("Users")
                        .child(uid)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                val users = dataSnapshot.getValue(UsersModel::class.java)
                                if (users != null) {
                                    users.img = uri.toString()
                                    dataSnapshot.ref.setValue(users)
                                }
                            }

                            override fun onCancelled(databaseError: DatabaseError) {}
                        })
                }
            }
        }
    }

    fun uploadName(name: String) {
        val map = HashMap<String, Any>()
        map["name"] = name
        val user = Firebase.auth.currentUser
        user?.let {
            val uid: String = user.uid
            FirebaseDatabase.getInstance().getReference("Users")
                .child(uid)
                .updateChildren(map)
        }
    }

}