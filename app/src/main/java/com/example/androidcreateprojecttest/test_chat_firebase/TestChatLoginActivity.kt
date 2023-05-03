package com.example.androidcreateprojecttest.test_chat_firebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.example.androidcreateprojecttest.data.model.Login
import com.example.androidcreateprojecttest.databinding.ActivityTestChatLogin2Binding
import com.example.androidcreateprojecttest.test_chat_firebase.view_model.TestChatLoginVM
import com.example.androidcreateprojecttest.util.displaySnackBar
import com.example.androidcreateprojecttest.util.progressBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import java.util.regex.Pattern

@AndroidEntryPoint
class TestChatLoginActivity : AppCompatActivity() {
    val auth = FirebaseAuth.getInstance()

    var email: String? = null
    var password: String? = null
    private lateinit var binding : ActivityTestChatLogin2Binding

    private val viewModel by viewModels<TestChatLoginVM>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestChatLogin2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        setViewListeners()

        viewModel.login(Login(email = "email", password = "123456"))
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
        binding.progress.progressBar(true)
        val fireStore = FirebaseFirestore.getInstance()
        fireStore.collection(Constant.KEY_COLLECTION_USERS)
            .whereEqualTo(Constant.KEY_EMAIL, email)
            .whereEqualTo(Constant.KEY_PASSWORD, password)
            .get()
            .addOnCompleteListener {
                binding.progress.progressBar(false)
                binding.buttonLogin.isEnabled = true
                if (it.isSuccessful && it.result != null && it.result.documentChanges.size > 0) {
                    val documentSnapshot = it.result.documents[0]

                    PreferenceManager.saveBoolean(Constant.KEY_IS_SIGNED_IN, true, this)
                    PreferenceManager.saveString(Constant.KEY_USER_ID, documentSnapshot.id, this)
                    PreferenceManager.saveString(
                        Constant.KEY_NAME,
                        documentSnapshot.get(Constant.KEY_NAME) as String?,
                        this
                    )
                    PreferenceManager.saveString(
                        Constant.KEY_EMAIL,
                        documentSnapshot.get(Constant.KEY_EMAIL) as String?,
                        this
                    )
                    PreferenceManager.saveString(
                        Constant.KEY_PASSWORD,
                        documentSnapshot.get(Constant.KEY_PASSWORD) as String?,
                        this
                    )
                    PreferenceManager.saveString(
                        Constant.KEY_IMAGE,
                        documentSnapshot.get(Constant.KEY_IMAGE) as String?,
                        this
                    )
                    launchHomeScreen()
                    binding.progress.displaySnackBar("You have already sign in your account.")
                } else {
                    binding.progress.displaySnackBar("This user is not found in our database.")
                }
            }
            .addOnFailureListener {
                binding.progress.progressBar(false)
                binding.buttonLogin.isEnabled = true
                it.message?.let { it1 -> binding.progress.displaySnackBar(it1) }
            }
    }

    private fun launchHomeScreen() {
        val intent = Intent(this, TestChatDisplayUserActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun launchRegister() {
        startActivity(Intent(this, TestChatSignUpActivity::class.java))
    }

    private fun showErrorMessage() {
        binding.textviewErrorLogin.visibility = View.VISIBLE
        binding.buttonLogin.isEnabled = true
    }
}