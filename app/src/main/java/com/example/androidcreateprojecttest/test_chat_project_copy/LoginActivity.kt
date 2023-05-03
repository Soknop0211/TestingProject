package com.example.androidcreateprojecttest.test_chat_project_copy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.androidcreateprojecttest.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {

    val auth = FirebaseAuth.getInstance()

    var email: String? = null
    var password: String? = null
    private lateinit var binding : ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setViewListeners()
    }

    private fun setViewListeners() {
        binding.buttonLogin.setOnClickListener { submit() }
        binding.textviewRegister.setOnClickListener { launchRegister() }
    }

    private fun submit() {
        binding.buttonLogin.isEnabled = false

        email = binding.edittextEmail.text.toString()
        password = binding.edittextPassword.text.toString()

        if (validate()) {
            login()
        } else {
            showErrorMessage()
        }
    }

    private fun validate(): Boolean {
        return !email.isNullOrEmpty()
            && !password.isNullOrEmpty()
            && isEmailValid(email!!)
    }

    private fun isEmailValid(email: String): Boolean {
        val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }

    private fun login() {
        auth.signInWithEmailAndPassword(email!!, password!!)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    launchLobby()
                } else {
                    showErrorMessage()
                }
            }
    }

    private fun launchLobby() {
        val intent = Intent(this, LobbyActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun launchRegister() {
        startActivity(Intent(this, RegisterActivity::class.java))
    }

    private fun showErrorMessage() {
        binding.textviewErrorLogin.visibility = View.VISIBLE
        binding.buttonLogin.isEnabled = true
    }

}
