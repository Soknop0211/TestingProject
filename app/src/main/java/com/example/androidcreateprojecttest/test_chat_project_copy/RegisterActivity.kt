package com.example.androidcreateprojecttest.test_chat_project_copy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.androidcreateprojecttest.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {

    val auth = FirebaseAuth.getInstance()

    var email: String? = null
    var password: String? = null
    var confirmPassword: String? = null

    private lateinit var binding : ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setViewListeners()
    }

    private fun setViewListeners() {
        binding.buttonRegister.setOnClickListener { submit() }
        binding.textviewLogin.setOnClickListener { launchLogin() }
    }

    private fun submit() {
        binding.buttonRegister.isEnabled = false

        email = binding.edittextEmail.text.toString()
        password = binding.edittextPassword.text.toString()
        confirmPassword = binding.edittextConfirmPassword.text.toString()

        if (validate()) {
            register()
        } else {
            showErrorMessage()
        }
    }

    private fun validate(): Boolean {
        return !email.isNullOrEmpty()
                && !password.isNullOrEmpty()
                && !confirmPassword.isNullOrEmpty()
                && password == confirmPassword
                && isEmailValid(email!!)
    }

    private fun isEmailValid(email: String): Boolean {
        val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }

    private fun register() {
        auth.createUserWithEmailAndPassword(email!!, password!!)
            .addOnCompleteListener {
                if (it.isSuccessful)
                    launchLobby()
                else
                    showErrorMessage()
            }
    }

    private fun launchLobby() {
        val intent = Intent(this, LobbyActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun launchLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
    }

    private fun showErrorMessage() {
        binding.textviewErrorRegister.visibility = View.VISIBLE
        binding.buttonRegister.isEnabled = true
    }

}
