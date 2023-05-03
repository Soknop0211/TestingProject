package com.example.androidcreateprojecttest.test_chat_firebase

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.androidcreateprojecttest.R
import com.google.firebase.auth.FirebaseAuth

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        checkLoggedIn()

    }

    private fun checkLoggedIn() {
        if (PreferenceManager.getBoolean(Constant.KEY_IS_SIGNED_IN, this)) {
            launchDisplay()
        } else {
            this.startBackLoginScreen()
        }
    }

    private fun launchDisplay() {
        startActivity(Intent(this, TestChatDisplayUserActivity::class.java))
        finish()
    }
}