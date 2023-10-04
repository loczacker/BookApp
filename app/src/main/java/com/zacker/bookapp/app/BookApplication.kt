package com.zacker.bookapp.app

import android.app.Application
import com.google.firebase.FirebaseApp

class BookApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}