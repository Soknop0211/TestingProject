package com.example.androidcreateprojecttest.test_chat_project_copy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.example.androidcreateprojecttest.R
import com.example.androidcreateprojecttest.databinding.ActivityLobbyBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LobbyActivity : AppCompatActivity() {

    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser
    val firestore = FirebaseFirestore.getInstance()
    private lateinit var binding : ActivityLobbyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLobbyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkUser()
        setViewListeners()
    }

    private fun setViewListeners() {
        binding.buttonEnter.setOnClickListener { enterRoom() }


        binding.signOutBtn.setOnClickListener {
            val user = auth.currentUser

            if (user != null) {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }

    private fun enterRoom() {
        binding.buttonEnter.isEnabled = false
        val roomId = binding.edittextRoomid.text.toString()

        if (roomId.isEmpty()) {
            showErrorMessage()
            return
        }

        firestore.collection("users").document(user!!.uid).collection("rooms")
            .document(roomId).set(mapOf(
                Pair("id", roomId)
            ))

        val intent = Intent(this, RoomActivity::class.java)
        intent.putExtra("INTENT_EXTRA_ROOMID", roomId)
        startActivity(intent)
    }

    private fun showErrorMessage() {
        binding.textviewErrorEnter.visibility = View.VISIBLE
        binding.buttonEnter.isEnabled = true
    }

    private fun checkUser() {
        if (user == null)
            launchLogin()
    }

    private fun launchLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun signOut() {
        auth.signOut()
        launchLogin()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.ic_sign_out) {
            signOut()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onStart() {
        super.onStart()
        binding.buttonEnter.isEnabled = true
    }
}
