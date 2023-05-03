package com.example.androidcreateprojecttest.test_chat_firebase

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.androidcreateprojecttest.databinding.ActivityTestChatSignUpBinding
import com.example.androidcreateprojecttest.util.displaySnackBar
import com.example.androidcreateprojecttest.util.progressBar
import com.google.firebase.firestore.FirebaseFirestore
import java.util.regex.Pattern

class TestChatSignUpActivity : AppCompatActivity() {

    var email: String? = null
    var password: String? = null
    var confirmPassword: String? = null
    var name: String? = null

    private lateinit var binding : ActivityTestChatSignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestChatSignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setViewListeners()
    }

    private fun setViewListeners() {
        binding.buttonRegister.setOnClickListener { submit() }
        binding.textviewLogin.setOnClickListener { this.startBackLoginScreen() }
    }

    private fun submit() {
        binding.buttonRegister.isEnabled = false

        email = binding.edittextEmail.text.toString()
        password = binding.edittextPassword.text.toString()
        confirmPassword = binding.edittextConfirmPassword.text.toString()
        name = binding.edittextName.text.toString()

        if (validate()) {
            register()
        } else {
            showErrorMessage()
        }
    }

    private fun validate(): Boolean {
        return !name.isNullOrEmpty()
                &&!email.isNullOrEmpty()
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
        binding.progress.progressBar(true)
        val fireStore = FirebaseFirestore.getInstance()
        val hashMap = HashMap<String, Any>()
        hashMap[Constant.KEY_NAME] = name!!
        hashMap[Constant.KEY_EMAIL] = email!!
        hashMap[Constant.KEY_PASSWORD] = password!!
        hashMap[Constant.KEY_IMAGE] = "https://sandboxeazy.daikou.asia/storage/2a5ef387876e90b52300aac52d8d1128.jpg"

        fireStore.collection(Constant.KEY_COLLECTION_USERS)
            .add(hashMap)
            .addOnSuccessListener {
                binding.progress.progressBar(false)
                binding.buttonRegister.isEnabled = true
                // PreferenceManager.saveBoolean(Constant.KEY_IS_SIGNED_IN, true, this)
                // PreferenceManager.saveString(Constant.KEY_USER_ID, it.id, this)
                // PreferenceManager.saveString(Constant.KEY_NAME, name, this)
                // PreferenceManager.saveString(Constant.KEY_EMAIL, email, this)
                // PreferenceManager.saveString(Constant.KEY_PASSWORD, password, this)
                this.startBackLoginScreen()
                binding.edittextEmail.displaySnackBar("You have already sign up new account .")
            }
            .addOnFailureListener {
                binding.progress.progressBar(false)
                binding.buttonRegister.isEnabled = true
                it.message?.let { it1 -> binding.edittextEmail.displaySnackBar(it1) }
            }
    }

    private fun showErrorMessage() {
        binding.textviewErrorRegister.visibility = View.VISIBLE
        binding.buttonRegister.isEnabled = true
    }
}

fun Context.startBackLoginScreen() {
    val intent = Intent(this, TestChatLoginActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    startActivity(intent)
}